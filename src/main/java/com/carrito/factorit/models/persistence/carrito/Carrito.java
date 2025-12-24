package com.carrito.factorit.models.persistence.carrito;

import com.carrito.factorit.enums.CarritoStatus;
import com.carrito.factorit.models.persistence.cliente.Cliente;
import com.carrito.factorit.models.persistence.productoCarrito.ProductoCarrito;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "carritos")
public class Carrito {

    public Carrito(Cliente cliente, boolean esEspecial) {
        this.cliente = cliente;
        this.esEspecial = esEspecial;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaCierre;

    @Column(nullable = false)
    private boolean esEspecial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarritoStatus estado;

    @Column(name = "total_pago", nullable = false, scale = 2)
    private BigDecimal totalPago;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoCarrito> productos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.totalPago=BigDecimal.ZERO;
        this.estado=CarritoStatus.ABIERTO;
        this.fechaCreacion = LocalDateTime.now();
    }
}
