package com.devsu.challenge.dto.reporte;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDTO
{
	private Integer clienteId;

	private String nombre;

	private String identificacion;

	private List<CuentaImprDTO> cuentas;

	private Double saldoTotalGeneral;
	private Double creditoTotalGeneral;
	private Double debitoTotalGeneral;

}
