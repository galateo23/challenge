package com.devsu.challenge.controller;

import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.entity.Cuenta;
import com.devsu.challenge.service.ICuentaService;
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
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cuentas")
public class CuentaController
{
	@Autowired
	private ICuentaService cuentaService;

	@GetMapping
	public ResponseEntity<List<CuentaDTO>> findAll()
	{
		List<CuentaDTO> clientes = cuentaService.findAll();
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CuentaDTO> findById(@PathVariable final Integer id)
	{
		return ResponseEntity.ok(cuentaService.findById(id));
	}

	@PostMapping
	public ResponseEntity<CuentaDTO> create(@Valid @RequestBody final CuentaDTO cuentaDTO, BindingResult bindingResult)
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
		CuentaDTO newcuentaDTO = cuentaService.create(cuentaDTO);
		log.info(Constants.CUENTA_CREADA);
		return ResponseEntity.status(HttpStatus.CREATED).body(newcuentaDTO);
	}

	@PutMapping
	public ResponseEntity<CuentaDTO> update(@RequestBody final CuentaDTO cuentaDTO, BindingResult bindingResult)
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
		CuentaDTO newcuentaAcutalizada = cuentaService.update(cuentaDTO);
		log.info(Constants.CUENTA_ACTUALIZADA);
		return ResponseEntity.status(HttpStatus.OK).body(newcuentaAcutalizada);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CuentaDTO> updatePatch(@PathVariable final Integer id, @RequestBody final Map<String, Object> results)
	{
		CuentaDTO cuentaActualizada = cuentaService.updatePatch(id, results);
		log.info(Constants.CUENTA_ACTUALIZADA);
		return ResponseEntity.status(HttpStatus.OK).body(cuentaActualizada);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id)
	{
		cuentaService.delete(id);
	}
}
