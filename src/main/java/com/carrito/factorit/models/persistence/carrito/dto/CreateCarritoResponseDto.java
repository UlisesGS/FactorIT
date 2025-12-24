package com.carrito.factorit.models.persistence.carrito.dto;

import com.carrito.factorit.enums.CarritoStatus;
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
public class CreateCarritoResponseDto {

    private LocalDateTime fechaCreacion;
    private boolean esEspecial;
    private CarritoStatus estado;
    private BigDecimal totalPago;

}
