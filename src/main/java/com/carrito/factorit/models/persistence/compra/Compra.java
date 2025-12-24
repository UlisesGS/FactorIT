package com.carrito.factorit.models.persistence.compra;


import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.descuentoAplicado.DescuentoAplicado;
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
@Table(name = "compras")
public class Compra {

    public Compra(Carrito carrito, BigDecimal totalPago) {
        this.carrito = carrito;
        this.totalPago = totalPago;
        this.descuentos = new ArrayList<>();;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_cierre", nullable = false)
    private LocalDateTime fechaCierre;

    @Column(name = "total_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CARRITO"))
    private Carrito carrito;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DescuentoAplicado> descuentos;

    @PrePersist
    public void prePersist() {
        this.fechaCierre = LocalDateTime.now();
    }
}
