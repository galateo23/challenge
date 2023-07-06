package com.devsu.challenge.controller;

import com.devsu.challenge.dto.MovimientoDTO;
import com.devsu.challenge.entity.Movimiento;
import com.devsu.challenge.service.IMovimientoService;
import com.devsu.challenge.util.BadRequestException;
import com.devsu.challenge.util.Constants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movimientos")
public class MovimientoController
{
	@Autowired
	private IMovimientoService movimientoService;

	@GetMapping
	public ResponseEntity<List<MovimientoDTO>> findAll()
	{
		List<MovimientoDTO> clientes = movimientoService.findAll();
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MovimientoDTO> findById(@PathVariable final Integer id)
	{
		return ResponseEntity.ok(movimientoService.findById(id));
	}

	@PostMapping
	public ResponseEntity<MovimientoDTO> create(@Valid @RequestBody final MovimientoDTO movimientoDTO, BindingResult bindingResult)
	{
		StringBuilder errors = new StringBuilder();
		if (bindingResult.hasErrors())
		{
			bindingResult.getAllErrors().forEach(objectError -> {
				errors.append(objectError.getDefaultMessage());
				errors.append("\n");
			});
			throw new BadRequestException(errors.toString());
		}
		MovimientoDTO newMovimientoDTO = movimientoService.create(movimientoDTO);
		log.info(Constants.MOVIMIENTO_CREADO);
		return ResponseEntity.status(HttpStatus.CREATED).body(newMovimientoDTO);
	}

	@PutMapping
	public ResponseEntity<MovimientoDTO> update(@RequestBody final MovimientoDTO movimientoDTO,  BindingResult bindingResult)
	{
		StringBuilder errors = new StringBuilder();
		if (bindingResult.hasErrors())
		{
			bindingResult.getAllErrors().forEach(objectError -> {
				errors.append(objectError.getDefaultMessage());
				errors.append("\n");
			});
			throw new BadRequestException(errors.toString());
		}
		MovimientoDTO movimientoDTOAcutalizado = movimientoService.update(movimientoDTO);
		log.info(Constants.MOVIMIENTO_ACTUALIZADO);
		return ResponseEntity.status(HttpStatus.OK).body(movimientoDTOAcutalizado);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id)
	{
		movimientoService.delete(id);
	}
}
