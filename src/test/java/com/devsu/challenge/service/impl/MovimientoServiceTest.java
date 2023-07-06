package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.dto.MovimientoDTO;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.entity.Movimiento;
import com.devsu.challenge.enums.MovimientoType;
import com.devsu.challenge.repository.MovimientoRepository;
import com.devsu.challenge.util.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest
{

	@Mock
	private MovimientoRepository mockMovimientoRepository;
	@Mock
	private CuentaService mockCuentaService;

	@InjectMocks
	private MovimientoService movimientoServiceUnderTest;


	@Test
	void testFindAll_MovimientoRepositoryReturnsNoItems()
	{
		when(mockMovimientoRepository.findAll()).thenReturn(Collections.emptyList());

		final List<MovimientoDTO> result = movimientoServiceUnderTest.findAll();

		assertThat(result).isEqualTo(Collections.emptyList());
	}



	@Test
	void testFindById_MovimientoRepositoryReturnsAbsent()
	{
		when(mockMovimientoRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> movimientoServiceUnderTest.findById(0)).isInstanceOf(NotFoundException.class);
	}



	@Test
	void testCreate_CuentaServiceReturnsAbsent()
	{
		final MovimientoDTO movimiento = new MovimientoDTO();
		movimiento.setId(2);
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		movimiento.setTipoMovimiento(MovimientoType.DEBITO);
		movimiento.setValor(0.0);
		movimiento.setSaldo(0.0);
		final CuentaDTO cuenta = new CuentaDTO();
		cuenta.setNumeroCuenta("5655353666");
		movimiento.setCuenta(cuenta);

		when(mockCuentaService.findByNumeroCuenta("5655353666")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> movimientoServiceUnderTest.create(movimiento)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testGetUltimoMovimiento()
	{
		final Movimiento movimiento = new Movimiento();
		movimiento.setId(0);
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		movimiento.setTipoMovimiento(MovimientoType.DEBITO);
		movimiento.setValor(0.0);
		movimiento.setSaldo(0.0);
		final Cuenta cuenta = new Cuenta();
		cuenta.setNumeroCuenta("87398999");
		cuenta.setSaldoInicial(0.0);
		cuenta.setMovimientos(List.of(new Movimiento()));
		movimiento.setCuenta(cuenta);
		final List<Movimiento> movimientos = List.of(movimiento);

		final double result = movimientoServiceUnderTest.getUltimoMovimiento(movimientos);

		assertThat(result).isEqualTo(0.0, within(0.0001));
	}



	@Test
	void testUpdate_MovimientoRepositoryFindByIdReturnsAbsent()
	{
		final MovimientoDTO movimiento = new MovimientoDTO();
		movimiento.setId(0);
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		movimiento.setTipoMovimiento(MovimientoType.DEBITO);
		movimiento.setValor(0.0);
		movimiento.setSaldo(0.0);
		final CuentaDTO cuenta = new CuentaDTO();
		cuenta.setNumeroCuenta("76365555");
		movimiento.setCuenta(cuenta);

		when(mockMovimientoRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> movimientoServiceUnderTest.update(movimiento)).isInstanceOf(NotFoundException.class);
	}


	@Test
	void testDelete()
	{
		final Movimiento movimiento1 = new Movimiento();
		movimiento1.setId(0);
		movimiento1.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		movimiento1.setTipoMovimiento(MovimientoType.DEBITO);
		movimiento1.setValor(0.0);
		movimiento1.setSaldo(0.0);
		final Cuenta cuenta = new Cuenta();
		cuenta.setNumeroCuenta("86355345");
		cuenta.setSaldoInicial(0.0);
		cuenta.setMovimientos(List.of(new Movimiento()));
		movimiento1.setCuenta(cuenta);
		final Optional<Movimiento> movimiento = Optional.of(movimiento1);
		when(mockMovimientoRepository.findById(0)).thenReturn(movimiento);

		movimientoServiceUnderTest.delete(0);

		verify(mockMovimientoRepository).deleteById(0);
	}

	@Test
	void testDelete_MovimientoRepositoryFindByIdReturnsAbsent()
	{
		when(mockMovimientoRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> movimientoServiceUnderTest.delete(0)).isInstanceOf(NotFoundException.class);
	}
}
