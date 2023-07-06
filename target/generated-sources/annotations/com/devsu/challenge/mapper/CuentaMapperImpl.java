package com.devsu.challenge.mapper;

import com.devsu.challenge.dto.ClienteDTO;
import com.devsu.challenge.dto.CuentaDTO;
import com.devsu.challenge.entity.Cliente;
import com.devsu.challenge.entity.Cuenta;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-05T15:02:41-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class CuentaMapperImpl implements CuentaMapper {

    @Override
    public CuentaDTO cuentaToCuentaDTO(Cuenta cuenta) {
        if ( cuenta == null ) {
            return null;
        }

        CuentaDTO cuentaDTO = new CuentaDTO();

        cuentaDTO.setId( cuenta.getId() );
        cuentaDTO.setNumeroCuenta( cuenta.getNumeroCuenta() );
        cuentaDTO.setTipoCuenta( cuenta.getTipoCuenta() );
        cuentaDTO.setSaldoInicial( cuenta.getSaldoInicial() );
        cuentaDTO.setEstado( cuenta.getEstado() );
        cuentaDTO.setCliente( clienteToClienteDTO( cuenta.getCliente() ) );

        return cuentaDTO;
    }

    @Override
    public Cuenta cuentaDTOToCuenta(CuentaDTO cuentaDTO) {
        if ( cuentaDTO == null ) {
            return null;
        }

        Cuenta cuenta = new Cuenta();

        cuenta.setId( cuentaDTO.getId() );
        cuenta.setNumeroCuenta( cuentaDTO.getNumeroCuenta() );
        cuenta.setTipoCuenta( cuentaDTO.getTipoCuenta() );
        if ( cuentaDTO.getSaldoInicial() != null ) {
            cuenta.setSaldoInicial( cuentaDTO.getSaldoInicial() );
        }
        cuenta.setEstado( cuentaDTO.getEstado() );
        cuenta.setCliente( clienteDTOToCliente( cuentaDTO.getCliente() ) );

        return cuenta;
    }

    protected ClienteDTO clienteToClienteDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        ClienteDTO clienteDTO = new ClienteDTO();

        clienteDTO.setClienteId( cliente.getClienteId() );
        clienteDTO.setNombre( cliente.getNombre() );
        clienteDTO.setGenero( cliente.getGenero() );
        clienteDTO.setEdad( cliente.getEdad() );
        clienteDTO.setIdentificacion( cliente.getIdentificacion() );
        clienteDTO.setDireccion( cliente.getDireccion() );
        clienteDTO.setTelefono( cliente.getTelefono() );
        clienteDTO.setEstado( cliente.getEstado() );
        clienteDTO.setContrasena( cliente.getContrasena() );

        return clienteDTO;
    }

    protected Cliente clienteDTOToCliente(ClienteDTO clienteDTO) {
        if ( clienteDTO == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setClienteId( clienteDTO.getClienteId() );
        cliente.setNombre( clienteDTO.getNombre() );
        cliente.setGenero( clienteDTO.getGenero() );
        cliente.setEdad( clienteDTO.getEdad() );
        cliente.setIdentificacion( clienteDTO.getIdentificacion() );
        cliente.setDireccion( clienteDTO.getDireccion() );
        cliente.setTelefono( clienteDTO.getTelefono() );
        cliente.setContrasena( clienteDTO.getContrasena() );
        cliente.setEstado( clienteDTO.getEstado() );

        return cliente;
    }
}
