package com.carrito.factorit.mapper.compra;

import com.carrito.factorit.models.persistence.compra.Compra;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;

public interface CompraMapper {
    CompraResponseDto toResponse(Compra compra);

}
