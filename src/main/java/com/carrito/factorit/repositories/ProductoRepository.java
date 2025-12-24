package com.carrito.factorit.repositories;

import com.carrito.factorit.models.persistence.producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
