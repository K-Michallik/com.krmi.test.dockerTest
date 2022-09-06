package com.ur.urcap.examples.dockerdaemon.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.docker.ContainerStatus;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

public class DockerDaemonInstallationNodeContribution implements InstallationNodeContribution {
	private static final String POPUPTITLE_KEY = "popuptitle";

	private static final String XMLRPC_VARIABLE = "my_daemon_swing";
	private static final String ENABLED_KEY = "enabled";
	private static final String DEFAULT_VALUE = "Hello Docker Daemon";

	private final DockerDaemonInstallationNodeView view;
	private final DockerDaemonService daemonService;
	private final InputValidationFactory inputValidationFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	private final DataModel model;

	private Timer uiTimer;
	private boolean pauseTimer = false;

	public DockerDaemonInstallationNodeContribution(InstallationAPIProvider apiProvider, DockerDaemonInstallationNodeView view, DataModel model, DockerDaemonService daemonService, CreationContext context) {
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		inputValidationFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
		this.view = view;
		this.daemonService = daemonService;
		this.model = model;
		if (context.getNodeCreationType() == CreationContext.NodeCreationType.NEW) {
			model.set(POPUPTITLE_KEY, DEFAULT_VALUE);
		}
		applyDesiredDaemonStatus();
	}

	@Override
	public void openView() {
		view.setPopupText(getPopupTitle());

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!pauseTimer) {
							updateUI();
						}
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() {
		if (uiTimer != null) {
			uiTimer.cancel();
		}
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.assign(XMLRPC_VARIABLE, "rpc_factory(\"xmlrpc\", \"http://127.0.0.1:" + getHostPortMapping() + "/RPC2\")");
		// Apply the settings to the daemon on program start in the Installation pre-amble
		writer.appendLine(XMLRPC_VARIABLE + ".set_title(\"" + getPopupTitle() + "\")");
	}

	private void updateUI() {
		ContainerStatus state = getDaemonState();

		if (state == state.RUNNING) {
			view.setStartButtonEnabled(false);
			view.setStopButtonEnabled(true);
		} else {
			view.setStartButtonEnabled(true);
			view.setStopButtonEnabled(false);
		}

		String text = "";
		switch (state) {
		case RUNNING:
			text = "Docker Daemon runs";
			break;
		case STOPPED:
			text = "Docker Daemon stopped";
			break;
		case ERROR:
			text = "Docker Daemon failed";
			break;
		}

		view.setStatusLabel(text);
	}

	public void onStartClick() {
		model.set(ENABLED_KEY, true);
		applyDesiredDaemonStatus();
	}

	public void onStopClick() {
		model.set(ENABLED_KEY, false);
		applyDesiredDaemonStatus();
	}

	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (isDaemonEnabled()) {
					// Download the daemon settings to the daemon process on initial start for real-time preview purposes
					try {
						pauseTimer = true;
						awaitDaemonRunning(5000);
						getXmlRpcDaemonInterface().setTitle(getPopupTitle());
					} catch(Exception e){
						System.err.println("Could not set the title in the daemon process.");
					} finally {
						pauseTimer = false;
					}
				} else {
					daemonService.getDaemon().stop();
				}
			}
		}).start();
	}

	private void awaitDaemonRunning(long timeOutMilliSeconds) throws InterruptedException {
		daemonService.getDaemon().start();
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while(System.nanoTime() < endTime && (daemonService.getDaemon().getContainerStatus() != ContainerStatus.RUNNING || !getXmlRpcDaemonInterface().isReachable())) {
			Thread.sleep(100);
		}
	}

	public String getPopupTitle() {
		return model.get(POPUPTITLE_KEY, DEFAULT_VALUE);
	}

	public KeyboardTextInput getInputForTextField() {
		KeyboardTextInput keyboardInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardInput.setErrorValidator(inputValidationFactory.createStringLengthValidator(1, 255));
		keyboardInput.setInitialValue(getPopupTitle());
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setPopupTitle(value);
				view.setPopupText(value);
			}
		};
	}

	private void setPopupTitle(String title) {
		model.set(POPUPTITLE_KEY, title);
		// Apply the new setting to the daemon for real-time preview purposes
		// Note this might influence a running program, since the actual state is stored in the daemon.
		try {
			getXmlRpcDaemonInterface().setTitle(title);
		} catch(Exception e){
			System.err.println("Could not set the title in the daemon process.");
		}
	}

	public boolean isDefined() {
		return model.isSet(POPUPTITLE_KEY) && getDaemonState() == ContainerStatus.RUNNING;
	}

	private ContainerStatus getDaemonState() {
		return daemonService.getDaemon().getContainerStatus();
	}

	private Boolean isDaemonEnabled() {
		return model.get(ENABLED_KEY, true); //This daemon is enabled by default
	}

	public String getXMLRPCVariable(){
		return XMLRPC_VARIABLE;
	}

	public XmlRpcMyDaemonInterface getXmlRpcDaemonInterface() {
		return new XmlRpcMyDaemonInterface("127.0.0.1", getHostPortMapping());
	}

	public int getHostPortMapping() {
		return daemonService.getDaemon().getHostPortMapping("xmlrpc");
	}
}
