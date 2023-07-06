package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.entity.Cliente;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.enums.CuentaType;
import com.devsu.challenge.mapper.CuentaMapper;
import com.devsu.challenge.repository.CuentaRepository;
import com.devsu.challenge.service.ICuentaService;
import com.devsu.challenge.util.ConflictException;
import com.devsu.challenge.util.Constants;
import com.devsu.challenge.util.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class CuentaService implements ICuentaService
{
	public static final String TIPO_CUENTA = "tipoCuenta";
	public static final String NUMERO_CUENTA = "numeroCuenta";

	@Autowired
	private CuentaRepository cuentaRepository;

	@Autowired
	private ClienteService clienteService;

	@Override
	public List<CuentaDTO> findAll()
	{
		return cuentaRepository.findAll().stream().map(CuentaMapper.INSTANCE::cuentaToCuentaDTO).collect(Collectors.toList());
	}

	@Override
	public CuentaDTO findById(final Integer id)
	{
		Optional<Cuenta> cuenta = cuentaRepository.findById(id);
		if (cuenta.isPresent())
		{
			return cuenta.map(CuentaMapper.INSTANCE::cuentaToCuentaDTO).get();
		}
		else
		{
			log.error(Constants.CUENTA_CON_ID_NO_EXISTE, id);
			throw new NotFoundException(String.format(Constants.CUENTA_CON_ID_S_NO_EXISTE, id));
		}
	}

	@Override
	public CuentaDTO create(final CuentaDTO cuentaDTO)
	{
		log.info(Constants.CREANDO_CUENTA);
		Optional<Cliente> cliente = clienteService.findByIdentificacion(cuentaDTO.getCliente().getIdentificacion());
		if (cliente.isEmpty())
		{
			log.error(Constants.CLIENTE_CON_ID_NO_EXISTE, cuentaDTO.getCliente());
			throw new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, cuentaDTO.getCliente()));
		}
		if (findByNumeroCuenta(cuentaDTO.getNumeroCuenta()).isPresent())
		{
			log.error(Constants.CUENTA_CON_NUMERO_DE_CUENTA_YA_EXISTE, cuentaDTO.getNumeroCuenta());
			throw new ConflictException(
					String.format(Constants.CUENTA_CON_NUMERO_DE_CUENTA_S_YA_EXISTE, cuentaDTO.getNumeroCuenta()));
		}
		Cuenta cuenta = CuentaMapper.INSTANCE.cuentaDTOToCuenta(cuentaDTO);
		cuenta.setCliente(cliente.get());
		return CuentaMapper.INSTANCE.cuentaToCuentaDTO(cuentaRepository.save(cuenta));
	}

	@Override
	public CuentaDTO update(final CuentaDTO cuentaDTO)
	{
		log.info(Constants.ACTUALIZANDO_CUENTA);
		Optional<Cuenta> updateCuenta = cuentaRepository.findById(cuentaDTO.getId());
		if (updateCuenta.isPresent())
		{
			if (!cuentaDTO.getNumeroCuenta().equals(updateCuenta.get().getNumeroCuenta()) && Objects.nonNull(
					findByNumeroCuenta(cuentaDTO.getNumeroCuenta())))
			{
				log.error(Constants.CUENTA_CON_NUMERO_DE_CUENTA_YA_EXISTE, cuentaDTO.getNumeroCuenta());
				throw new ConflictException(
						String.format(Constants.CUENTA_CON_NUMERO_DE_CUENTA_S_YA_EXISTE, cuentaDTO.getNumeroCuenta()));
			}
			Optional<Cliente> cliente = clienteService.findByIdentificacion(cuentaDTO.getCliente().getIdentificacion());
			final Cuenta cuenta = CuentaMapper.INSTANCE.cuentaDTOToCuenta(cuentaDTO);
			cuenta.setCliente(cliente.get());
			return CuentaMapper.INSTANCE.cuentaToCuentaDTO(cuentaRepository.save(cuenta));
		}
		else
		{
			log.error(Constants.CUENTA_CON_ID_NO_EXISTE, cuentaDTO.getId());
			throw new NotFoundException(String.format(Constants.CUENTA_CON_ID_S_NO_EXISTE, cuentaDTO.getId()));
		}
	}

	@Override
	public CuentaDTO updatePatch(final Integer id, final Map<String, Object> results)
	{
		log.info(Constants.ACTUALIZANDO_CUENTA);
		Optional<Cuenta> cuenta = cuentaRepository.findById(id);
		if (cuenta.isPresent())
		{

			results.forEach((k, v) -> {
				if (k.equals(NUMERO_CUENTA) && !v.equals(cuenta.get().getNumeroCuenta()) && findByNumeroCuenta(
						v.toString()).isPresent())
				{
					log.error(Constants.CUENTA_CON_NUMERO_DE_CUENTA_YA_EXISTE, v.toString());
					throw new ConflictException(String.format(Constants.CUENTA_CON_NUMERO_DE_CUENTA_S_YA_EXISTE, v.toString()));
				}

				if (k.equals(TIPO_CUENTA))
				{
					v = CuentaType.valueOf(v.toString());
				}

				Field field = ReflectionUtils.findField(Cuenta.class, k);
				field.setAccessible(true);
				ReflectionUtils.setField(field, cuenta.get(), v);
			});
			return CuentaMapper.INSTANCE.cuentaToCuentaDTO(cuentaRepository.save(cuenta.get()));
		}
		else
		{
			log.error(Constants.CUENTA_CON_ID_NO_EXISTE, id);
			throw new NotFoundException(String.format(Constants.CUENTA_CON_ID_S_NO_EXISTE, id));
		}
	}

	@Override
	public void delete(final Integer id)
	{
		cuentaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format(Constants.CUENTA_CON_ID_S_NO_EXISTE, id)));
		cuentaRepository.deleteById(id);
		log.info(Constants.CUENTA_ELIMINADA, id);
	}

	@Override
	public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta)
	{
		return cuentaRepository.findByNumeroCuenta(numeroCuenta);
	}
}
