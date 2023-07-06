package com.devsu.challenge.entity;

import com.devsu.challenge.enums.GeneroType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona implements Serializable
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cliente_id")
	private Integer clienteId;

	@NotEmpty(message = "Nombre es un campo obligatorio no puede estar vacio")
	private String nombre;

	@NotNull(message = "Genero es un campo obligatorio y solo puede ser MACULINO, FEMENINO O NO_ESPECIFICA")
	@Enumerated(EnumType.STRING)
	private GeneroType genero;


	@NotNull(message = "Edad es un campo obligatorio no puede estar vacio")
	@Range(min=1, max=120,message = "Edad minima es 1 y la edad maxima es 120")
	private byte edad;

	@NotEmpty(message = "Identificacion es un campo obligatorio no puede estar vacio")
	@Column(unique = true)
	@Pattern(regexp = "^[0-9]{10,13}$", message = "Numero de identificación invalido y debe tener entre 10 y 13 numeros")
	private String identificacion;

	@NotEmpty(message = "Direccion es un campo obligatorio no puede estar vacio")
	private String direccion;

	@NotEmpty(message = "Telefono es un campo obligatorio no puede estar vacio")
	@Pattern(regexp = "^[0-9]{9,13}$", message = "Numero de Telefono invalido y debe tener entre 9 y 13 numeros")
	private String telefono;

}