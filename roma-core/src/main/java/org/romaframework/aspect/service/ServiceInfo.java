package org.romaframework.aspect.service;

import java.util.List;

public class ServiceInfo {
	private String											url;
	private String											operationName;
	private List<ServiceParameterInfo>	input;
	private List<ServiceParameterInfo>	output;

	public ServiceInfo() {
	}

	/**
	 * 
	 * @param operationName
	 */
	public ServiceInfo(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getOperationName() {
		return operationName;
	}
	
	/**
	 * 
	 * @param operationName
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	/**
	 * 
	 * @return List
	 */
	public List<ServiceParameterInfo> getInput() {
		return input;
	}

	/**
	 * 
	 * @param input
	 */
	public void setInput(List<ServiceParameterInfo> input) {
		this.input = input;
	}

	/**
	 * 
	 * @return List
	 */
	public List<ServiceParameterInfo> getOutput() {
		return output;
	}

	/**
	 * 
	 * @param output
	 */
	public void setOutput(List<ServiceParameterInfo> output) {
		this.output = output;
	}
}
