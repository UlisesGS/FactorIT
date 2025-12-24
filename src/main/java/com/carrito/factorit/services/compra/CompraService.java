package com.carrito.factorit.services.compra;

import com.carrito.factorit.enums.CompraOrdenFiltro;
import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import com.carrito.factorit.models.persistence.compra.dto.PageCompraResponseDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CompraService {
    CompraResponseDto crearCompra(Carrito carrito, ResultadoDescuento resultado);
    PageCompraResponseDto listAllProduct(String dni, LocalDate desde, LocalDate hasta, CompraOrdenFiltro orden, Pageable pageable);
    boolean esClienteVip(String dni, LocalDateTime fecha);
}
