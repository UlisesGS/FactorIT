package com.carrito.factorit.services.cliente;

import com.carrito.factorit.exceptions.ResourceNotFoundException;
import com.carrito.factorit.models.persistence.cliente.Cliente;
import com.carrito.factorit.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    @Override
    public boolean clienteExiste(String dni) {
        return this.clienteRepository.existsByDni(dni);
    }

    @Override
    public Cliente findByDni(String dni) {
        return clienteRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }


}
