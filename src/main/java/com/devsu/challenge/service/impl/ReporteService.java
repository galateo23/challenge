package com.devsu.challenge.service.impl;

import com.devsu.challenge.dto.reporte.CuentaImprDTO;
import com.devsu.challenge.dto.reporte.MovimientoImprDTO;
import com.devsu.challenge.dto.reporte.ReporteDTO;
import com.devsu.challenge.entity.Cliente;
import com.devsu.challenge.entity.Movimiento;
import com.devsu.challenge.service.IReporteService;
import com.devsu.challenge.util.Constants;
import com.devsu.challenge.util.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class ReporteService implements IReporteService
{
	@Autowired
	private ClienteService clienteService;

	@Override
	public ReporteDTO getReporte(Integer clienteId, LocalDate fechaInicial, LocalDate fechafinal)
	{
		final Cliente cliente = clienteService.getMovByClienteId(clienteId, fechaInicial, fechafinal);

		if (Objects.isNull(cliente))
		{
			log.error(Constants.CLIENTE_CON_ID_NO_EXISTE, clienteId);
			throw new NotFoundException(String.format(Constants.CLIENTE_CON_ID_S_NO_EXISTE, clienteId));
		}

		ReporteDTO reporteDTO = new ReporteDTO();
		reporteDTO.setClienteId(cliente.getClienteId());
		reporteDTO.setNombre(cliente.getNombre());
		reporteDTO.setIdentificacion(cliente.getIdentificacion());

		List<CuentaImprDTO> cuentaReporteDTOList = cliente.getCuentas().stream().map(cuenta -> {
			CuentaImprDTO cuentaReporteDTO = new CuentaImprDTO();
			cuentaReporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
			cuentaReporteDTO.setTipoCuenta(cuenta.getTipoCuenta().toString());
			cuentaReporteDTO.setSaldo(cuenta.getSaldoInicial());

			List<MovimientoImprDTO> movimientosReporteDTO = cuenta.getMovimientos().stream().map(m -> {
				MovimientoImprDTO movimientoReporteDTO = new MovimientoImprDTO();
				movimientoReporteDTO.setFecha(m.getFecha());
				movimientoReporteDTO.setTipoMovimiento(m.getTipoMovimiento());
				movimientoReporteDTO.setValor(m.getValor());
				movimientoReporteDTO.setSaldo(m.getSaldo());
				return movimientoReporteDTO;
			}).collect(Collectors.toList());

			double totalDebitos = cuenta.getMovimientos().stream()
					.filter(m -> Constants.DEBITO.equals(m.getTipoMovimiento().getValue())).mapToDouble(Movimiento::getValor)
					.sum();

			double totalCreditos = cuenta.getMovimientos().stream()
					.filter(m -> Constants.CREDITO.equals(m.getTipoMovimiento().getValue())).mapToDouble(Movimiento::getValor)
					.sum();

			cuentaReporteDTO.setMovimientos(movimientosReporteDTO);
			cuentaReporteDTO.setTotalCreditos(totalCreditos);
			cuentaReporteDTO.setTotalDebitos(totalDebitos);
			cuentaReporteDTO.setSaldo(cuentaReporteDTO.getSaldo() + (totalCreditos - totalDebitos));
			return cuentaReporteDTO;
		}).collect(Collectors.toList());

		reporteDTO.setCuentas(cuentaReporteDTOList);
		reporteDTO.setSaldoTotalGeneral(cuentaReporteDTOList.stream().mapToDouble(CuentaImprDTO::getSaldo).sum());
		reporteDTO.setCreditoTotalGeneral(cuentaReporteDTOList.stream().mapToDouble(CuentaImprDTO::getTotalCreditos).sum());
		reporteDTO.setDebitoTotalGeneral(cuentaReporteDTOList.stream().mapToDouble(CuentaImprDTO::getTotalDebitos).sum());

		log.info(Constants.REPORTE_GENERADO);
		return reporteDTO;
	}
}
