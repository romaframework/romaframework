package org.romaframework.aspect.persistence;

public enum QueryOperator {

	LIKE("LIKE"), NOT_EQUALS("<>"), MAJOR_EQUALS(">="), MINOR_EQUALS("<="), MAJOR(">"), MINOR("<"), EQUALS("="), CONTAINS("contains"), IN("in"), NOT_IN("not in");

	private String	conditionValue;

	private QueryOperator(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getConditionValue() {
		return conditionValue;
	}
}