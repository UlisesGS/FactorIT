package com.carrito.factorit.services.cliente;

import com.carrito.factorit.models.persistence.cliente.Cliente;


public interface ClienteService {
    boolean clienteExiste(String dni);
    Cliente findByDni(String dni);
}
