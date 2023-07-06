package com.devsu.challenge.service;

import com.devsu.challenge.dto.reporte.ReporteDTO;

import java.time.LocalDate;

public interface IReporteService
{
	ReporteDTO getReporte(Integer clienteId, LocalDate fechaInicial, LocalDate fechafinal);

}
