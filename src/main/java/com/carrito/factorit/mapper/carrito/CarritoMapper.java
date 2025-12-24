package com.carrito.factorit.mapper.carrito;

import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.carrito.dto.CreateCarritoResponseDto;

public interface CarritoMapper {
    CreateCarritoResponseDto toResponse(Carrito carrito);
}
