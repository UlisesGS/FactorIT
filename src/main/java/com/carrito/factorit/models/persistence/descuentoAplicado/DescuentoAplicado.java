package com.carrito.factorit.models.persistence.descuentoAplicado;

import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.models.persistence.compra.Compra;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "descuentos_aplicados")
public class DescuentoAplicado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoDescuento tipo;

    @Column(nullable = false)
    private BigDecimal monto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CARRITO"))
    private Compra compra;
}
