package com.devsu.challenge.repository;

import com.devsu.challenge.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Integer>
{
	Optional<Cuenta> findByNumeroCuenta(final String numeroCuenta);

}
