package com.devsu.challenge.mapper;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClienteMapper
{
	ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

	ClienteDTO clienteToClienteDTO(final Cliente cliente);
	Cliente clienteDTOToCliente(final ClienteDTO clienteDTO);

}
