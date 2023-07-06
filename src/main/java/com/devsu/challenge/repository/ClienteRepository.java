package com.devsu.challenge.repository;

import com.devsu.challenge.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>
{
	Optional<Cliente> findByIdentificacion(final String identificacion);
	Optional<Cliente> findByClienteId(final Integer clienteId);
	void deleteByClienteId(final Integer clienteId);
}
