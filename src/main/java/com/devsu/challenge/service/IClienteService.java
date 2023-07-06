package com.devsu.challenge.service;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.entity.Cliente;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IClienteService
{
	List<ClienteDTO> findAll();

	ClienteDTO findByClienteId(final Integer clienteId);

	ClienteDTO create(final ClienteDTO clienteDTO);

	ClienteDTO update(final ClienteDTO clienteDTO);

	ClienteDTO updatePatch(final Integer clienteId, final Map<String, Object> results);

	void delete(final Integer clienteId);

	Optional<Cliente> findByIdentificacion(final String identificacion);

	Cliente getMovByClienteId(final Integer clienteId, final LocalDate fechaInicial, final LocalDate fechaFinal);

}
