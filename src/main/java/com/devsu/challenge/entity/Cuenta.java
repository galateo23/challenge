package com.devsu.challenge.entity;

import com.devsu.challenge.enums.CuentaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuentas")
public class Cuenta implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty(message = "Numero Cuenta es un campo obligatorio")
	@Pattern(regexp = "^[0-9]{6,11}$", message = "Verificar el número de la cuenta")
	@Column(name = "numero_cuenta", unique = true)
	private String numeroCuenta;

	@NotNull(message = "Tipo Cuenta es un campo obligatorio y solo puede ser AHORROS O CORRIENTE")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cuenta")
	private CuentaType tipoCuenta;

	@NotNull(message = "Saldo Inicial es un campo obligatorio")
	@Column(name = "saldo_inicial")
	private double saldoInicial;

	@NotNull(message = "Estado es un campo obligatorio y solo puede ser true o false")
	private Boolean estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@OneToMany(mappedBy = "cuenta", fetch = FetchType.LAZY)
	private List<Movimiento> movimientos;
}
