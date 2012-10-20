package org.romaframework.module.users.domain;

public class ActivityLogCategory {

	private String	name;

	public ActivityLogCategory() {
		this(null);
	}

	public ActivityLogCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
