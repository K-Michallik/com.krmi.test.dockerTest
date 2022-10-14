package com.ur.urcap.examples.dockerdaemon.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class DockerDaemonInstallationNodeService 
		implements SwingInstallationNodeService<DockerDaemonInstallationNodeContribution, DockerDaemonInstallationNodeView> {

	private final DockerDaemonService daemonService;

	public DockerDaemonInstallationNodeService(DockerDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "Modbus RTU Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public DockerDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new DockerDaemonInstallationNodeView(style);
	}

	@Override
	public DockerDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, DockerDaemonInstallationNodeView view, 
			DataModel model, CreationContext context) {
		return new DockerDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, context);
	}

}
