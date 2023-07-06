package com.devsu.challenge.mapper;

import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CuentaMapper
{
	CuentaMapper INSTANCE = Mappers.getMapper(CuentaMapper.class);

	CuentaDTO cuentaToCuentaDTO(final Cuenta cuenta);
	Cuenta cuentaDTOToCuenta(final CuentaDTO cuentaDTO);

}
