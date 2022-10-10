package com.ur.urcap.examples.dockerdaemon.impl;

import com.ur.urcap.api.contribution.docker.DockerRegistrationService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
// import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) {
		DockerDaemonService daemonService = new DockerDaemonService();
		DockerDaemonInstallationNodeService installationNodeService = new DockerDaemonInstallationNodeService(daemonService);

		context.registerService(DockerRegistrationService.class, daemonService, null);
		context.registerService(SwingInstallationNodeService.class, installationNodeService, null);
		// context.registerService(SwingProgramNodeService.class, new DockerDaemonProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) {
	}
}
