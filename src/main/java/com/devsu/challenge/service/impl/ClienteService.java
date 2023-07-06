package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.entity.Cliente;
import com.devsu.challenge.entity.Movimiento;
import com.devsu.challenge.enums.GeneroType;
import com.devsu.challenge.mapper.ClienteMapper;
import com.devsu.challenge.repository.ClienteRepository;
import com.devsu.challenge.service.IClienteService;
import com.devsu.challenge.util.ConflictException;
import com.devsu.challenge.util.Constants;
import com.devsu.challenge.util.InternalServerErrorException;
import com.devsu.challenge.util.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class ClienteService implements IClienteService
{
	public static final String CONTRASENA = "contrasena";
	public static final String GENERO = "genero";
	public static final String EDAD = "edad";
	public static final String IDENTIFICACION = "identificacion";

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public List<ClienteDTO> findAll()
	{
		return clienteRepository.findAll().stream().map(ClienteMapper.INSTANCE::clienteToClienteDTO).collect(Collectors.toList());
	}

	@Override
	public ClienteDTO findByClienteId(final Integer clienteId)
	{
		Optional<Cliente> cliente = clienteRepository.findByClienteId(clienteId);
		if (cliente.isPresent())
		{
			return cliente.map(ClienteMapper.INSTANCE::clienteToClienteDTO).get();
		}
		else
		{
			log.error(Constants.CLIENTE_CON_ID_NO_EXISTE, clienteId);
			throw new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, clienteId));
		}
	}

	@Override
	public ClienteDTO create(final ClienteDTO clienteDTO)
	{
		log.info(Constants.CREANDO_CLIENTE);
		if (findByIdentificacion(clienteDTO.getIdentificacion()).isPresent())
		{
			log.error(Constants.CLIENTE_CON_NUMERO_DE_IDENTIFICACION_YA_EXISTE, clienteDTO.getIdentificacion());
			throw new ConflictException(
					String.format(Constants.CLIENTE_CON_NUMERO_DE_IDENTIFICACION_S_YA_EXISTE, clienteDTO.getIdentificacion()));
		}
		clienteDTO.setContrasena(cifrarPassword(clienteDTO.getContrasena()));
		return ClienteMapper.INSTANCE.clienteToClienteDTO(
				clienteRepository.save(ClienteMapper.INSTANCE.clienteDTOToCliente(clienteDTO)));
	}

	@Override
	public ClienteDTO update(final ClienteDTO clienteDTO)
	{
		log.info(Constants.ACTUALIZANDO_CLIENTE);
		Optional<Cliente> updatecliente = clienteRepository.findByClienteId(clienteDTO.getClienteId());
		if (updatecliente.isPresent())
		{
			if (!clienteDTO.getIdentificacion().equals(updatecliente.get().getIdentificacion()) && findByIdentificacion(
					clienteDTO.getIdentificacion()).isPresent())
			{
				log.error(Constants.CLIENTE_CON_NUMERO_DE_IDENTIFICACION_YA_EXISTE, clienteDTO.getIdentificacion());
				throw new ConflictException(String.format(Constants.CLIENTE_CON_NUMERO_DE_IDENTIFICACION_S_YA_EXISTE,
						clienteDTO.getIdentificacion()));
			}
			clienteDTO.setContrasena(cifrarPassword(clienteDTO.getContrasena()));
			return ClienteMapper.INSTANCE.clienteToClienteDTO(
					clienteRepository.save(ClienteMapper.INSTANCE.clienteDTOToCliente(clienteDTO)));
		}
		else
		{
			log.error(Constants.CLIENTE_CON_ID_NO_EXISTE, clienteDTO.getClienteId());
			throw new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, clienteDTO.getClienteId()));
		}
	}

	@Override
	public ClienteDTO updatePatch(final Integer clienteId, final Map<String, Object> results)
	{
		Optional<Cliente> cliente = clienteRepository.findByClienteId(clienteId);
		if (cliente.isPresent())
		{
			results.forEach((k, v) -> {
				if (k.equals(IDENTIFICACION) && !v.equals(cliente.get().getIdentificacion()) && findByIdentificacion(
						v.toString()).isPresent())
				{
					log.error(Constants.CLIENTE_CON_NUMERO_DE_IDENTIFICACION_YA_EXISTE, v.toString());
					throw new ConflictException(
							String.format(Constants.CLIENTE_CON_NUMERO_DE_IDENTIFICACION_S_YA_EXISTE, v.toString()));
				}

				if (k.equals(EDAD))
				{
					v = Byte.parseByte(v.toString());
				}
				if (k.equals(GENERO))
				{
					v = GeneroType.valueOf(v.toString());
				}
				if (k.equals(CONTRASENA))
				{
					v = cifrarPassword(v.toString());
				}
				Field field = ReflectionUtils.findField(Cliente.class, k);
				field.setAccessible(true);
				ReflectionUtils.setField(field, cliente.get(), v);
			});
			return ClienteMapper.INSTANCE.clienteToClienteDTO(clienteRepository.save(cliente.get()));
		}
		else
		{
			log.error(Constants.CLIENTE_CON_ID_NO_EXISTE, clienteId);
			throw new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, clienteId));
		}

	}

	@Override
	public void delete(final Integer clienteId)
	{
		clienteRepository.findByClienteId(clienteId)
				.orElseThrow(() -> new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, clienteId)));
		clienteRepository.deleteByClienteId(clienteId);
		log.info(Constants.CLIENTE_ELIMINADO, clienteId);

	}

	@Override
	public Optional<Cliente> findByIdentificacion(final String identificacion)
	{
		return clienteRepository.findByIdentificacion(identificacion);
	}

	@Override
	public Cliente getMovByClienteId(final Integer clienteId, final LocalDate fechaInicial, final LocalDate fechaFinal)
	{
		Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> {
			log.warn(Constants.CLIENTE_CON_ID_NO_EXISTE, clienteId);
			return new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, clienteId));
		});

		cliente.getCuentas().forEach(cuenta -> {
			List<Movimiento> movimientoList = cuenta.getMovimientos().stream()
					.filter(m -> m.getFecha().toLocalDate().isEqual(fechaInicial) || m.getFecha().toLocalDate()
							.isEqual(fechaFinal) || (m.getFecha().toLocalDate().isAfter(fechaInicial) && m.getFecha()
							.toLocalDate().isBefore(fechaFinal))).collect(Collectors.toList());
			cuenta.setMovimientos(movimientoList);
		});
		return cliente;
	}

	private String cifrarPassword(final String password)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Error cifrando la contraseña");
			throw new InternalServerErrorException("Error cifrando la contraseña");
		}

		byte[] hash = md.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();

		for (byte b : hash)
		{
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
}
