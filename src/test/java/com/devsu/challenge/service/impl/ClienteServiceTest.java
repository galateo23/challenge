package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.entity.Cliente;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.entity.Movimiento;
import com.devsu.challenge.enums.GeneroType;
import com.devsu.challenge.mapper.ClienteMapper;
import com.devsu.challenge.repository.ClienteRepository;
import com.devsu.challenge.util.ConflictException;
import com.devsu.challenge.util.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest
{

	@Mock
	private ClienteRepository mockClienteRepository;

	@InjectMocks
	private ClienteService clienteServiceUnderTest;



	@Test
	void testFindAll_ClienteRepositoryReturnsNoItems()
	{
		when(mockClienteRepository.findAll()).thenReturn(Collections.emptyList());

		final List<ClienteDTO> result = clienteServiceUnderTest.findAll();

		assertThat(result).isEqualTo(Collections.emptyList());
	}



	@Test
	void testFindByClienteId_ClienteRepositoryReturnsAbsent()
	{
		when(mockClienteRepository.findByClienteId(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> clienteServiceUnderTest.findByClienteId(0)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testCreate_ThrowsConflictException()
	{
		final ClienteDTO clienteDTO = new ClienteDTO();
		clienteDTO.setClienteId(0);
		clienteDTO.setIdentificacion("Francis");
		clienteDTO.setContrasena("1234567");
		final Cuenta cuenta = new Cuenta();
		final Movimiento movimiento = new Movimiento();
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta.setMovimientos(List.of(movimiento));
		Cliente cliente = ClienteMapper.INSTANCE.clienteDTOToCliente(clienteDTO);
		cliente.setCuentas(List.of(cuenta));

		final Cliente cliente2 = new Cliente();
		cliente2.setClienteId(0);
		cliente2.setIdentificacion("Francis");
		cliente2.setContrasena("1234567");
		final Cuenta cuenta1 = new Cuenta();
		final Movimiento movimiento1 = new Movimiento();
		movimiento1.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta1.setMovimientos(List.of(movimiento1));
		cliente2.setCuentas(List.of(cuenta1));
		final Optional<Cliente> cliente1 = Optional.of(cliente2);
		when(mockClienteRepository.findByIdentificacion("Francis")).thenReturn(cliente1);

		assertThatThrownBy(() -> clienteServiceUnderTest.create(clienteDTO)).isInstanceOf(ConflictException.class);
	}



	@Test
	void testUpdate_ClienteRepositoryFindByClienteIdReturnsAbsent()
	{
		final ClienteDTO cliente = new ClienteDTO();
		cliente.setClienteId(0);
		cliente.setIdentificacion("123454");
		cliente.setContrasena("cris09876373");
		when(mockClienteRepository.findByClienteId(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> clienteServiceUnderTest.update(cliente)).isInstanceOf(NotFoundException.class);
	}



	@Test
	void testUpdatePatch_ClienteRepositoryFindByClienteIdReturnsAbsent()
	{
		final Map<String, Object> results = Map.ofEntries(Map.entry("value", "value"));
		when(mockClienteRepository.findByClienteId(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> clienteServiceUnderTest.updatePatch(0, results)).isInstanceOf(NotFoundException.class);
	}


	@Test
	void testDelete()
	{
		final Cliente cliente1 = new Cliente();
		cliente1.setClienteId(0);
		cliente1.setIdentificacion("Brian");
		cliente1.setContrasena("123432123");
		final Cuenta cuenta = new Cuenta();
		final Movimiento movimiento = new Movimiento();
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta.setMovimientos(List.of(movimiento));
		cliente1.setCuentas(List.of(cuenta));
		final Optional<Cliente> cliente = Optional.of(cliente1);
		when(mockClienteRepository.findByClienteId(0)).thenReturn(cliente);

		clienteServiceUnderTest.delete(0);

		verify(mockClienteRepository).deleteByClienteId(0);
	}

	@Test
	void testDelete_ClienteRepositoryFindByClienteIdReturnsAbsent()
	{
		when(mockClienteRepository.findByClienteId(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> clienteServiceUnderTest.delete(0)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void testFindByIdentificacion()
	{
		final Cliente cliente = new Cliente();
		cliente.setClienteId(0);
		cliente.setIdentificacion("1234333");
		cliente.setContrasena("robemjj09888");
		final Cuenta cuenta = new Cuenta();
		final Movimiento movimiento = new Movimiento();
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta.setMovimientos(List.of(movimiento));
		cliente.setCuentas(List.of(cuenta));
		final Optional<Cliente> expectedResult = Optional.of(cliente);

		final Cliente cliente2 = new Cliente();
		cliente2.setClienteId(0);
		cliente2.setIdentificacion("1234333");
		cliente2.setContrasena("robemjj09888");
		final Cuenta cuenta1 = new Cuenta();
		final Movimiento movimiento1 = new Movimiento();
		movimiento1.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta1.setMovimientos(List.of(movimiento1));
		cliente2.setCuentas(List.of(cuenta1));
		final Optional<Cliente> cliente1 = Optional.of(cliente2);
		when(mockClienteRepository.findByIdentificacion("1234333")).thenReturn(cliente1);

		final Optional<Cliente> result = clienteServiceUnderTest.findByIdentificacion("1234333");

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	void testFindByIdentificacion_ClienteRepositoryReturnsAbsent()
	{
		when(mockClienteRepository.findByIdentificacion("384746664")).thenReturn(Optional.empty());

		final Optional<Cliente> result = clienteServiceUnderTest.findByIdentificacion("384746664");

		assertThat(result).isEmpty();
	}

	@Test
	void testGetMovByClienteId()
	{
		final Cliente expectedResult = new Cliente();
		expectedResult.setClienteId(0);
		expectedResult.setIdentificacion("63553636");
		expectedResult.setContrasena("123");
		final Cuenta cuenta = new Cuenta();
		final Movimiento movimiento = new Movimiento();
		movimiento.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta.setMovimientos(List.of(movimiento));
		expectedResult.setCuentas(List.of(cuenta));

		final Cliente cliente1 = new Cliente();
		cliente1.setClienteId(0);
		cliente1.setIdentificacion("63553636");
		cliente1.setContrasena("123");
		final Cuenta cuenta1 = new Cuenta();
		final Movimiento movimiento1 = new Movimiento();
		movimiento1.setFecha(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		cuenta1.setMovimientos(List.of(movimiento1));
		cliente1.setCuentas(List.of(cuenta1));
		final Optional<Cliente> cliente = Optional.of(cliente1);
		when(mockClienteRepository.findById(0)).thenReturn(cliente);

		final Cliente result = clienteServiceUnderTest.getMovByClienteId(0, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	void testGetMovByClienteId_ClienteRepositoryReturnsAbsent()
	{
		when(mockClienteRepository.findById(0)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> clienteServiceUnderTest.getMovByClienteId(0, LocalDate.of(2020, 1, 1),
				LocalDate.of(2020, 1, 1))).isInstanceOf(NotFoundException.class);
	}
}
