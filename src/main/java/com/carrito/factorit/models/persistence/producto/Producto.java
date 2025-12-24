package com.carrito.factorit.models.persistence.producto;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "codigo_producto", nullable = false)
    private String codigoProducto;

    @Column(nullable = false)
    private Integer stock;

    @DecimalMin(value = "100.00", inclusive = true, message = "El precio mínimo es 100")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
}
