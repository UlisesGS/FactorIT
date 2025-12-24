package com.carrito.factorit.models.persistence.productoCarrito.dto;

import com.carrito.factorit.models.persistence.producto.dto.ProductoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarritoResponseDto {
    private List<ProductoResponseDto> productoResponseDtoList;
    private Integer cantidad;
}
