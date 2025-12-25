package com.carrito.factorit.models.persistence.productoCarrito;

import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "productos_carrito")
public class ProductoCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PRODUCTO"))
    private Producto producto;


    @Column(nullable = false)
    private Integer cantidad;
}
