package com.carrito.factorit.models.persistence.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductoResponseDto {
    private Long id;
    private BigDecimal totalPago;
}
