package com.devsu.challenge.service;

import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.entity.Cuenta;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICuentaService
{
	List<CuentaDTO> findAll();

	CuentaDTO findById(final Integer id);

	CuentaDTO create(final CuentaDTO cuentaDTO);

	CuentaDTO update(final CuentaDTO cuentaDTO);

	CuentaDTO updatePatch(final Integer id, final Map<String, Object> results);

	void delete(final Integer id);

	Optional<Cuenta> findByNumeroCuenta(final String numeroCuenta);

}
