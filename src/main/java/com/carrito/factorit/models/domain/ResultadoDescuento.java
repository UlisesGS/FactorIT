package com.carrito.factorit.models.domain;

import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.models.persistence.descuentoAplicado.DescuentoAplicado;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResultadoDescuento {

    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal totalFinal = BigDecimal.ZERO;
    private List<DescuentoAplicado> descuentos = new ArrayList<>();

    public void agregarDescuento(TipoDescuento tipo, BigDecimal monto) {
        if (monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            DescuentoAplicado d = new DescuentoAplicado();
            d.setTipo(tipo);
            d.setMonto(monto);
            descuentos.add(d);
        }
    }
}