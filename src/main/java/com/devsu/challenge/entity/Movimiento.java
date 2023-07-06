package com.devsu.challenge.entity;

import com.devsu.challenge.enums.MovimientoType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movimientos")
public class Movimiento
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDateTime fecha;

	@NotNull(message = "Tipo movimiento es un campo obligatorio y solo puede ser AHORROS O CORRIENTE")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_movimiento")
	private MovimientoType tipoMovimiento;

	@NotNull(message = "Valor es un campo obligatorio")
	private Double valor;

	private Double saldo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "numero_cuenta")
	private Cuenta cuenta;

}
