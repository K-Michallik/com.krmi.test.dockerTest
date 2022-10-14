package com.ur.urcap.examples.dockerdaemon.impl;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class XmlRpcMyDaemonInterface {

	private final XmlRpcClient client;

	public XmlRpcMyDaemonInterface(String host, int port) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setEnabledForExtensions(true);
		try {
			config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		config.setConnectionTimeout(1000); //1s
		client = new XmlRpcClient();
		client.setConfig(config);
	}

	public boolean isReachable() {
		try {
			// this.client.execute("reachable", new ArrayList<String>());
			this.client.execute("reachable", new ArrayList<String>());
			return true;
		} catch (XmlRpcException e) {
			return false;
		}
	}

	private boolean processBoolean(Object response) throws UnknownResponseException {
		if (response instanceof Boolean) {
			Boolean val = (Boolean) response;
			return val.booleanValue();
		} else {
			throw new UnknownResponseException();
		}
	}

	private String processString(Object response) throws UnknownResponseException {
		if (response instanceof String) {
			return (String) response;
		} else {
			throw new UnknownResponseException();
		}
	}
}
