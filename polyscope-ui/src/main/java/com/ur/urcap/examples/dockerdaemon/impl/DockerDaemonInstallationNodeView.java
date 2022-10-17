package com.ur.urcap.examples.dockerdaemon.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

public class DockerDaemonInstallationNodeView implements SwingInstallationNodeView<DockerDaemonInstallationNodeContribution> {
	
	private JButton startButton;
	private JButton stopButton;
	private JLabel statusLabel;
	private final Style style;

	public DockerDaemonInstallationNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, DockerDaemonInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createStatusLabel(contribution));

	}
	
	private Box createStatusLabel(final DockerDaemonInstallationNodeContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		Dimension dimension = new Dimension();
		dimension.setSize(100, 50);
		
		this.startButton = new JButton("Start Daemon");
		this.stopButton = new JButton("Stop Daemon");
		this.statusLabel = new JLabel("My Daemon status");
		
		this.startButton.setSize(dimension);
		this.stopButton.setSize(dimension);
		
		
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onStartClick();
			}
		});
		box.add(startButton);
	
		
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onStopClick();
			}
		});
		box.add(stopButton);
		
		
		box.add(startButton);
		box.add(createHorizontalSpacing(20));
		box.add(stopButton);
		box.add(createHorizontalSpacing(20));
		box.add(statusLabel);
		
		return box;
		
	}
	
	public void setStartButtonEnabled(boolean enabled) {
		startButton.setEnabled(enabled);
	}

	public void setStopButtonEnabled(boolean enabled) {
		stopButton.setEnabled(enabled);
	}

	public void setStatusLabel(String text) {
		statusLabel.setText(text);
	}
	
	/**
	 * Create a horizontal spacing.
	 * 
	 * @param spacesize
	 * @return
	 */
	private Component createHorizontalSpacing(int spacesize) {
		return Box.createRigidArea(new Dimension(spacesize, 0));
	}

}
