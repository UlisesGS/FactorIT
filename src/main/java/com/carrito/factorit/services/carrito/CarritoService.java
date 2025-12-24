package com.carrito.factorit.services.carrito;

import com.carrito.factorit.models.persistence.carrito.dto.*;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;

public interface CarritoService {
    CreateCarritoResponseDto createCarrito(CreateCarritoRequestDto createCarritoRequestDto);
    void deleteCarrito(String dni);
    AddProductoResponseDto addProducto(Long carritoId, AddProductoRequestDto addProductoRequestDto);
    AddProductoResponseDto deleteProducto(Long carritoId, AddProductoRequestDto addProductoRequestDto);
    void emptyCarrito(Long carritoId);
    ListProductoCarritoResponseDto checkStatusCarrito(Long carritoId);
    CompraResponseDto closeCarrito(Long carritoId);
}
