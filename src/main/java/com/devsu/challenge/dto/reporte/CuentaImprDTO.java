package com.devsu.challenge.dto.reporte;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CuentaImprDTO
{
	private String numeroCuenta;
	private String tipoCuenta;
	private Double saldo;
	private Double totalDebitos;
	private Double totalCreditos;
	private List<MovimientoImprDTO> movimientos;
}
