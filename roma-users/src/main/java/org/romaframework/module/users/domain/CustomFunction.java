package org.romaframework.module.users.domain;

public class CustomFunction {

	private String	name;
	private Boolean	allow;

	public CustomFunction(){
		
	}
	public CustomFunction(String function, Boolean allow) {
		this.name = function;
		this.allow = allow;
	}

	public String getName() {
		return name;
	}

	public void setName(String function) {
		this.name = function;
	}

	public Boolean isAllow() {
		return allow;
	}

	public void setAllow(Boolean allow) {
		this.allow = allow;
	}

}
