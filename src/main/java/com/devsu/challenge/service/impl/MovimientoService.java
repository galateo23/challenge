package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.MovimientoDTO;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.entity.Movimiento;
import com.devsu.challenge.mapper.MovimientoMapper;
import com.devsu.challenge.repository.MovimientoRepository;
import com.devsu.challenge.service.IMovimientoService;
import com.devsu.challenge.util.BadRequestException;
import com.devsu.challenge.util.Constants;
import com.devsu.challenge.util.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class MovimientoService implements IMovimientoService
{
	public static final String SALDO_NO_DISPONIBLE = "Saldo no disponible";
	public static final String CUPO_DIARIO_EXCEDIDO = "Cupo diario Excedido";
	public static final int VALOR_TOPE = 1000;
	@Autowired
	private MovimientoRepository movimientoRepository;

	@Autowired
	private CuentaService cuentaService;

	@Override
	public List<MovimientoDTO> findAll()
	{
		return movimientoRepository.findAll().stream().map(MovimientoMapper.INSTANCE::movimientoToMoviemientoDTO)
				.collect(Collectors.toList());
	}

	@Override
	public MovimientoDTO findById(Integer id)
	{
		Optional<Movimiento> movimiento = movimientoRepository.findById(id);
		if (movimiento.isPresent())
		{
			return movimiento.map(MovimientoMapper.INSTANCE::movimientoToMoviemientoDTO).get();
		}
		else
		{
			log.error(Constants.MOVIMIENTO_CON_ID_NO_EXISTE, id);
			throw new NotFoundException(String.format(Constants.MOVIMIENTO_CON_ID_S_NO_EXISTE, id));
		}
	}

	@Override
	public MovimientoDTO create(MovimientoDTO movimientoDTO)
	{
		log.info(Constants.CREANDO_MOVIMIENTO);
		final Optional<Cuenta> cuenta = cuentaService.findByNumeroCuenta(movimientoDTO.getCuenta().getNumeroCuenta());
		if (cuenta.isPresent())
		{
			final double total;
			if (cuenta.get().getMovimientos().isEmpty())
			{
				total = createMovimiento(movimientoDTO.getTipoMovimiento().getValue(), cuenta.get().getSaldoInicial(),
						movimientoDTO.getValor());
			}
			else
			{
				total = createMovimiento(movimientoDTO.getTipoMovimiento().getValue(),
						getUltimoMovimiento(cuenta.get().getMovimientos()), movimientoDTO.getValor());
			}
			final Movimiento movimiento = MovimientoMapper.INSTANCE.movimientoDTOToMovimiento(movimientoDTO);
			movimiento.setFecha(LocalDateTime.now());
			movimiento.setCuenta(cuenta.get());
			movimiento.setSaldo(total);
			validateLimit(cuenta.get().getMovimientos(), movimiento);
			return MovimientoMapper.INSTANCE.movimientoToMoviemientoDTO(movimientoRepository.save(movimiento));
		}
		else
		{
			log.error(Constants.CUENTA_CON_NUMERO_DE_CUENTA_NO_EXISTE, movimientoDTO.getCuenta().getNumeroCuenta());
			throw new NotFoundException(String.format(Constants.CUENTA_CON_NUMERO_DE_CUENTA_S_NO_EXISTE,
					movimientoDTO.getCuenta().getNumeroCuenta()));
		}
	}

	private void validateLimit(final List<Movimiento> movimientos, final Movimiento movimiento)
	{
		double limiteDia = movimientos.stream().filter(mov -> mov.getFecha().toLocalDate()
				.isEqual(movimiento.getFecha().toLocalDate()) && mov.getTipoMovimiento().getValue()
				.equalsIgnoreCase(Constants.CREDITO)).mapToDouble(Movimiento::getValor).sum();

		if (movimiento.getTipoMovimiento().getValue().equalsIgnoreCase(Constants.CREDITO))
		{
			limiteDia += movimiento.getValor();
		}
		if (limiteDia >= VALOR_TOPE)
		{
			log.error(CUPO_DIARIO_EXCEDIDO);
			throw new BadRequestException(CUPO_DIARIO_EXCEDIDO);
		}
	}

	private double createMovimiento(final String tipoMovimiento, final double saldo, final double valor)
	{
		double saldoNuevo = saldo;
		switch (tipoMovimiento)
		{
			case Constants.DEBITO:
				saldoNuevo -= valor;
				break;
			case Constants.CREDITO:
				saldoNuevo += valor;
				break;
		}

		if (saldoNuevo < 0)
		{
			log.error(SALDO_NO_DISPONIBLE);
			throw new BadRequestException(SALDO_NO_DISPONIBLE);
		}
		return saldoNuevo;
	}

	public double getUltimoMovimiento(List<Movimiento> movimientos)
	{
		return movimientos.isEmpty() ? 0 : movimientos.get(movimientos.size() - 1).getSaldo();
	}

	@Override
	public MovimientoDTO update(final MovimientoDTO movimientoDTO)
	{
		final Optional<Movimiento> updateMovimientoOpt = movimientoRepository.findById(movimientoDTO.getId());
		if (updateMovimientoOpt.isPresent())
		{
			final Movimiento updateMovimiento = updateMovimientoOpt.get();
			Optional<Cuenta> cuentaOpt = cuentaService.findByNumeroCuenta(movimientoDTO.getCuenta().getNumeroCuenta());
			double saldo = getSaldo(updateMovimiento, cuentaOpt);

			updateMovimiento.setSaldo(
					createMovimiento(movimientoDTO.getTipoMovimiento().getValue(), saldo, movimientoDTO.getValor()));
			updateMovimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
			updateMovimiento.setValor(movimientoDTO.getValor());
			validateLimit(cuentaOpt.get().getMovimientos().stream().filter(mov -> !mov.getId().equals(updateMovimiento.getId()))
					.toList(), updateMovimiento);
			return MovimientoMapper.INSTANCE.movimientoToMoviemientoDTO(movimientoRepository.save(updateMovimiento));
		}
		else
		{
			log.error(Constants.MOVIMIENTO_CON_ID_NO_EXISTE, movimientoDTO.getId());
			throw new NotFoundException(String.format(Constants.MOVIMIENTO_CON_ID_S_NO_EXISTE, movimientoDTO.getId()));
		}
	}

	private double getSaldo(Movimiento updateMovimiento, Optional<Cuenta> cuentaOpt)
	{
		double saldo = cuentaOpt.map(cuenta -> {
			double saldoNuevo = getUltimoMovimiento(cuenta.getMovimientos());
			if (updateMovimiento.getTipoMovimiento().getValue().equals(Constants.DEBITO))
			{
				saldoNuevo += updateMovimiento.getValor();
			}
			else if (updateMovimiento.getTipoMovimiento().getValue().equals(Constants.CREDITO))
			{
				saldoNuevo -= updateMovimiento.getValor();
			}
			return saldoNuevo;
		}).orElse(0.0);
		return saldo;
	}

	@Override
	public void delete(Integer id)
	{
		movimientoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format(Constants.MOVIMIENTO_CON_ID_S_NO_EXISTE, id)));
		movimientoRepository.deleteById(id);
		log.info(Constants.MOVIMIENTO_ELIMINADO, id);
	}

}
