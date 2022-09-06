package com.ur.urcap.examples.dockerdaemon.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class DockerDaemonProgramNodeService implements SwingProgramNodeService<DockerDaemonProgramNodeContribution, DockerDaemonProgramNodeView> {

	public DockerDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "DockerDaemonNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Docker Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public DockerDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new DockerDaemonProgramNodeView(style);
	}

	@Override
	public DockerDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, DockerDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new DockerDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
