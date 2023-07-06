package com.devsu.challenge.mapper;

import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.dto.MovimientoDTO;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.entity.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MovimientoMapper
{
	MovimientoMapper INSTANCE = Mappers.getMapper(MovimientoMapper.class);

	MovimientoDTO movimientoToMoviemientoDTO(final Movimiento movimiento);
	Movimiento movimientoDTOToMovimiento(final MovimientoDTO movimientoDTO);

}
