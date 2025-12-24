package com.carrito.factorit.repositories;

import com.carrito.factorit.enums.CarritoStatus;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByCliente_DniAndEstado(String dni, CarritoStatus estado);
}
