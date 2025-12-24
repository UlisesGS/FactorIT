package com.carrito.factorit.services.producto;

import com.carrito.factorit.models.persistence.producto.Producto;

public interface ProductoService {
    Producto findById(Long id);
}
