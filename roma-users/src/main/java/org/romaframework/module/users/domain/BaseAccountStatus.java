package org.romaframework.module.users.domain;

public class BaseAccountStatus {
	private String	name;

	public BaseAccountStatus() {
		this(null);
	}

	public BaseAccountStatus(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BaseAccountStatus))return false;
		return name == null ?((BaseAccountStatus)obj).name == null: name.equals(((BaseAccountStatus)obj).name);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
