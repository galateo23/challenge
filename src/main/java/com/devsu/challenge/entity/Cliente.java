package com.devsu.challenge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clientes")
public class Cliente extends Persona implements Serializable
{
	@NotEmpty(message = "Contrasena es un campo obligatorio no puede estar vacio")
	private String contrasena;

	@NotNull(message = "Estado es un campo obligatorio y solo puede ser true o false")
	private Boolean estado;

		@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		private List<Cuenta> cuentas;
}
