package com.devsu.challenge.service;

import com.devsu.challenge.dto.MovimientoDTO;
import com.devsu.challenge.entity.Movimiento;

import java.util.List;
import java.util.Map;

public interface IMovimientoService
{
	List<MovimientoDTO> findAll();

	MovimientoDTO findById(final Integer id);

	MovimientoDTO create(final MovimientoDTO movimientoDTO);

	MovimientoDTO update(final MovimientoDTO movimientoDTO);

	void delete(final Integer id);
}
