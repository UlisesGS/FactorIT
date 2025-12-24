package com.carrito.factorit.controllers;

import com.carrito.factorit.documentation.carrito.*;
import com.carrito.factorit.models.persistence.carrito.dto.*;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import com.carrito.factorit.services.carrito.CarritoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Validated
public class CarritoController {

    private final CarritoService carritoService;

    @CheckStatusCarritoDoc
    @GetMapping("/consultar-estado/{carritoId}")
    public ResponseEntity<ListProductoCarritoResponseDto> checkStatusCarrito(
            @Positive(message = "Error en el numero pasado por url")
            @PathVariable Long carritoId){
        ListProductoCarritoResponseDto listProductoCarritoResponseDto=carritoService.checkStatusCarrito(carritoId);
        return ResponseEntity.status(HttpStatus.OK).body(listProductoCarritoResponseDto);
    }

    @CreateCarritoDoc
    @PostMapping("/crear")
    public ResponseEntity<CreateCarritoResponseDto> createCarrito(@Valid @RequestBody CreateCarritoRequestDto createCarritoRequestDto){
        CreateCarritoResponseDto createCarritoResponseDto = carritoService.createCarrito(createCarritoRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCarritoResponseDto);
    }

    @CloseCarritoDoc
    @PostMapping("/cerrar/{carritoId}")
    public ResponseEntity<CompraResponseDto>  closeCarrito(
            @Positive(message = "Error en el numero pasado por url")
            @PathVariable Long carritoId){
        CompraResponseDto compraResponseDto = carritoService.closeCarrito(carritoId);
        return ResponseEntity.status(HttpStatus.OK).body(compraResponseDto);
    }

    @DeleteCarritoDoc
    @DeleteMapping("/eliminar/{dni}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarrito(
            @NotEmpty(message = "El DNI es obligatorio")
            @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
            @PathVariable
            String dni) {
        carritoService.deleteCarrito(dni);
    }

    @AddProductDoc
    @PostMapping("/agregar-producto/{carritoId}")
    public ResponseEntity<AddProductoResponseDto> addProducto(
            @Positive(message = "Error en el numero pasado por url")
            @PathVariable Long carritoId,
            @Valid @RequestBody AddProductoRequestDto addProductoRequestDto){
        AddProductoResponseDto addProductoResponseDto=carritoService.addProducto(carritoId, addProductoRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductoResponseDto);
    }

    @DeleteProductoDoc
    @DeleteMapping("/sacar-producto/{carritoId}")
    public ResponseEntity<AddProductoResponseDto> deleteproducto(
            @Positive(message = "Error en el numero pasado por url")
            @PathVariable Long carritoId,
            @Valid @RequestBody AddProductoRequestDto addProductoRequestDto){
        AddProductoResponseDto addProductoResponseDto=carritoService.deleteProducto(carritoId, addProductoRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(addProductoResponseDto);
    }

    @EmptyCarritoDoc
    @DeleteMapping("/vaciar/{carritoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void emptyCarrito(
            @Positive(message = "Error en el numero pasado por url")
            @PathVariable Long carritoId){
        carritoService.emptyCarrito(carritoId);
    }


}
