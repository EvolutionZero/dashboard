package com.zero.dashboard.enums;

public enum ExtremeTypeEnum {

	BOTTOM("低点", 1),
	TOP("高点", 2),
	;
	
	private String name;
	private int value;

	ExtremeTypeEnum(String name, int value){
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
