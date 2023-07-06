package com.devsu.challenge.enums;

public enum CuentaType
{
	AHORROS("Ahorros"), CORRIENTE("Corriente");

	private final String value;

	CuentaType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

}
