package com.carrito.factorit.mapper.carrito;

import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.carrito.dto.CreateCarritoResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CarritoMapperImpl implements  CarritoMapper{
    @Override
    public CreateCarritoResponseDto toResponse(Carrito carrito) {
        return new CreateCarritoResponseDto(
                carrito.getFechaCreacion(),
                carrito.isEsEspecial(),
                carrito.getEstado(),
                carrito.getTotalPago()
        );
    }
}
