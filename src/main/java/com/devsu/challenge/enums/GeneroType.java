package com.devsu.challenge.enums;

public enum GeneroType
{
	MASCULINO("Masculino"), FEMENINO("Femenino"),NO_ESPECIFICA("No Especifica");

	private final String value;

	GeneroType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

}
