package com.ur.urcap.examples.dockerdaemon.impl;

import com.ur.urcap.api.contribution.docker.DockerContribution;
import com.ur.urcap.api.contribution.docker.DockerRegistrationService;
import com.ur.urcap.api.contribution.docker.DockerRegistry;


public class DockerDaemonService implements DockerRegistrationService {

	private DockerContribution dockerContribution;

	public DockerDaemonService() {
	}

	public DockerContribution getDaemon() {
		return dockerContribution;
	}

	@Override
	public void registerDockerContributions(DockerRegistry dockerRegistry) {
		dockerContribution = dockerRegistry.registerDockerContribution("daemon-py");
	}
}
