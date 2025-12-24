package com.carrito.factorit.services.descuento;

import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;

import java.math.BigDecimal;

public interface DescuentoService {
    ResultadoDescuento calcularTotal(Carrito carrito);
    boolean aplicaVip(Carrito carrito, BigDecimal total);
}
