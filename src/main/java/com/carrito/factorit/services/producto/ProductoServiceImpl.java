package com.carrito.factorit.services.producto;

import com.carrito.factorit.exceptions.ResourceNotFoundException;
import com.carrito.factorit.models.persistence.producto.Producto;
import com.carrito.factorit.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado")
        );
    }
}
