package org.romaframework.module.users.domain;

import java.util.HashMap;
import java.util.Map;

public class CustomProfiling {

	private BaseAccount									account;

	private Map<String, CustomFunction>	functions;

	public CustomProfiling() {
		functions = new HashMap<String, CustomFunction>();
	}

	public BaseAccount getAccount() {
		return account;
	}

	public void setAccount(BaseAccount account) {
		this.account = account;
	}

	public Map<String, CustomFunction> getFunctions() {
		return functions;
	}

	public void setFunctions(Map<String, CustomFunction> functions) {
		this.functions = functions;
	}

}
