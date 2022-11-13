package com.zero.dashboard.enums;

public enum ExtremeLevelEnum {

	SHORT("短期", 1), MIDDLE("中期", 2), LONG("长期", 3);

	private String name;
	private int value;

	ExtremeLevelEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
}
