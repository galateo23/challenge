package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.entity.Cliente;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.enums.CuentaType;
import com.devsu.challenge.repository.CuentaRepository;
import com.devsu.challenge.util.ConflictException;
import com.devsu.challenge.util.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest
{

	@Mock
	private CuentaRepository mockCuentaRepository;
	@Mock
	private ClienteService mockClienteService;

	@InjectMocks
	private CuentaService cuentaServiceUnderTest;

	@Test
	void testFindAll_CuentaRepositoryReturnsNoItems()
	{
		when(mockCuentaRepository.findAll()).thenReturn(Collections.emptyList());

		final List<CuentaDTO> result = cuentaServiceUnderTest.findAll();

		assertThat(result).isEqualTo(Collections.emptyList());
	}

	@Test
	void testFindById_CuentaRepositoryReturnsAbsent()
	{
		when(mockCuentaRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cuentaServiceUnderTest.findById(0)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testUpdate_CuentaRepositoryFindByIdReturnsAbsent()
	{
		final CuentaDTO cuenta = new CuentaDTO();
		cuenta.setId(0);
		cuenta.setNumeroCuenta("45453322");
		cuenta.setTipoCuenta(CuentaType.AHORROS);
		final Cliente cliente = new Cliente();
		cliente.setIdentificacion("231234333");

		when(mockCuentaRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cuentaServiceUnderTest.update(cuenta)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testUpdatePatch_CuentaRepositoryFindByIdReturnsAbsent()
	{
		final Map<String, Object> results = Map.ofEntries(Map.entry("value", "value"));
		when(mockCuentaRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cuentaServiceUnderTest.updatePatch(0, results)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testDelete()
	{
		final Cuenta cuenta1 = new Cuenta();
		cuenta1.setId(0);
		cuenta1.setNumeroCuenta("342223333");
		cuenta1.setTipoCuenta(CuentaType.AHORROS);
		final Cliente cliente = new Cliente();
		cliente.setIdentificacion("09847874747");
		cuenta1.setCliente(cliente);
		final Optional<Cuenta> cuenta = Optional.of(cuenta1);
		when(mockCuentaRepository.findById(0)).thenReturn(cuenta);

		cuentaServiceUnderTest.delete(0);

		verify(mockCuentaRepository).deleteById(0);
	}

	@Test
	void testDelete_CuentaRepositoryFindByIdReturnsAbsent()
	{
		when(mockCuentaRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> cuentaServiceUnderTest.delete(0)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testFindByNumeroCuenta()
	{
		final Cuenta cuenta = new Cuenta();
		cuenta.setId(0);
		cuenta.setNumeroCuenta("7377737989");
		cuenta.setTipoCuenta(CuentaType.AHORROS);
		final Cliente cliente = new Cliente();
		cliente.setIdentificacion("114388383888");
		cuenta.setCliente(cliente);
		final Optional<Cuenta> expectedResult = Optional.of(cuenta);

		final Cuenta cuenta2 = new Cuenta();
		cuenta2.setId(0);
		cuenta2.setNumeroCuenta("7377737989");
		cuenta2.setTipoCuenta(CuentaType.AHORROS);
		final Cliente cliente1 = new Cliente();
		cliente1.setIdentificacion("114388383888");
		cuenta2.setCliente(cliente1);
		final Optional<Cuenta> cuenta1 = Optional.of(cuenta2);
		when(mockCuentaRepository.findByNumeroCuenta("7377737989")).thenReturn(cuenta1);

		final Optional<Cuenta> result = cuentaServiceUnderTest.findByNumeroCuenta("7377737989");

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	void testFindByNumeroCuenta_CuentaRepositoryReturnsAbsent()
	{
		when(mockCuentaRepository.findByNumeroCuenta("00093873873")).thenReturn(Optional.empty());

		final Optional<Cuenta> result = cuentaServiceUnderTest.findByNumeroCuenta("00093873873");

		assertThat(result).isEmpty();
	}
}
