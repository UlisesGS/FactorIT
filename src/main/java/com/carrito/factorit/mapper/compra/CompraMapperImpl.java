package com.carrito.factorit.mapper.compra;

import com.carrito.factorit.models.persistence.compra.Compra;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CompraMapperImpl implements CompraMapper{


    @Override
    public CompraResponseDto toResponse(Compra compra) {
        return new CompraResponseDto(
                compra.getId(),
                compra.getFechaCierre(),
                compra.getTotalPago(),
                compra.getCarrito().getId()
                );
    }
}
