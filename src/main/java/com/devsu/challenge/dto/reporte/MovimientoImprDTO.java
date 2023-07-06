package com.devsu.challenge.dto.reporte;

import com.devsu.challenge.enums.MovimientoType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovimientoImprDTO
{
	private MovimientoType tipoMovimiento;
	private LocalDateTime fecha;
	private Double valor;
	private Double saldo;
}
