package com.carrito.factorit.models.persistence.compra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDto {

    private Long id;

    private LocalDateTime fechaCierre;

    private BigDecimal totalPago;

    private Long carritoId;
}
