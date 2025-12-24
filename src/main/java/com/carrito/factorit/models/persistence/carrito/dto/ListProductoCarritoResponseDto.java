package com.carrito.factorit.models.persistence.carrito.dto;

import com.carrito.factorit.enums.CarritoStatus;
import com.carrito.factorit.models.persistence.productoCarrito.dto.ProductoCarritoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListProductoCarritoResponseDto {
    private List<ProductoCarritoResponseDto> productoCarritoResponseDtoList;
    private CarritoStatus estado;
    private Integer totalProductos;
}
