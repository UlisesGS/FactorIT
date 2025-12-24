package com.carrito.factorit.repositories;

import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.models.persistence.compra.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface CompraRepository  extends JpaRepository<Compra, Long> {

    @Query("""
        SELECT COALESCE(SUM(c.totalPago), 0)
        FROM Compra c
        JOIN c.carrito ca
        JOIN ca.cliente cl
        WHERE cl.dni = :dni
          AND c.fechaCierre BETWEEN :desde AND :hasta
    """)
    BigDecimal sumTotalByClienteAndFechaBetween(
            @Param("dni") String dni,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    @Query("""
    SELECT COUNT(d) > 0
    FROM DescuentoAplicado d
    JOIN d.compra c
    WHERE c.carrito.cliente.dni = :dni
      AND d.tipo = :tipo
      AND c.fechaCierre BETWEEN :inicio AND :fin
    """)
    boolean existsDescuentoUsadoEnMes(
            @Param("dni") String dni,
            @Param("tipo") TipoDescuento tipo,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );


    Page<Compra> findByCarrito_Cliente_Dni(
            String clienteDni,
            Pageable pageable
    );

    Page<Compra> findByCarrito_Cliente_DniAndFechaCierreBetween(
            String clienteDni,
            LocalDateTime desde,
            LocalDateTime hasta,
            Pageable pageable
    );
}
