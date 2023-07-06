package com.devsu.challenge.controller;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.service.IClienteService;
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
@RequestMapping("/clientes")
public class ClienteController
{
	@Autowired
	private IClienteService clienteService;

	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll()
	{
		List<ClienteDTO> clientes = clienteService.findAll();
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<ClienteDTO> findByClienteId(@PathVariable final Integer clienteId)
	{
		return ResponseEntity.ok(clienteService.findByClienteId(clienteId));
	}

	@PostMapping
	public ResponseEntity<ClienteDTO> create(@Valid @RequestBody final ClienteDTO clienteDTO, BindingResult bindingResult)
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
		ClienteDTO newclienteDTO = clienteService.create(clienteDTO);
		log.info(Constants.CLIENTE_CREADO);
		return ResponseEntity.status(HttpStatus.CREATED).body(newclienteDTO);
	}

	@PutMapping
	public ResponseEntity<ClienteDTO> update(@RequestBody final ClienteDTO clienteDTO,  BindingResult bindingResult)
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
		ClienteDTO clienteActualizado = clienteService.update(clienteDTO);
		log.info(Constants.CLIENTE_ACTUALIZADO);
		return ResponseEntity.status(HttpStatus.OK).body(clienteActualizado);
	}

	@PatchMapping("/{clienteId}")
	public ResponseEntity<ClienteDTO> updatePatch(@PathVariable final Integer clienteId,@RequestBody final Map<String, Object> results)
	{
		ClienteDTO clienteActualizado = clienteService.updatePatch(clienteId,results);
		return ResponseEntity.status(HttpStatus.OK).body(clienteActualizado);
	}

	@DeleteMapping("/{clienteId}")
	public void delete(@PathVariable Integer clienteId)
	{
		clienteService.delete(clienteId);
	}
}
