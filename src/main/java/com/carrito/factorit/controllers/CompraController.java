package com.carrito.factorit.controllers;

import com.carrito.factorit.documentation.compra.ListAllCompraDoc;
import com.carrito.factorit.enums.CompraOrdenFiltro;
import com.carrito.factorit.models.persistence.compra.dto.PageCompraResponseDto;
import com.carrito.factorit.services.compra.CompraService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @ListAllCompraDoc
    @GetMapping("/listar-compras/{dni}")
    public ResponseEntity<PageCompraResponseDto> listAllCompra(
            @NotEmpty(message = "El DNI es obligatorio")
            @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
            @PathVariable
            String dni,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @PastOrPresent(message = "La fecha 'desde' no puede ser futura")
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @PastOrPresent(message = "La fecha 'hasta' no puede ser futura")
            LocalDate hasta,

            @RequestParam(required = false, defaultValue = "FECHA")
            CompraOrdenFiltro orden,

            Pageable pageable

    ){
        PageCompraResponseDto response = compraService.listAllProduct(dni,desde,hasta,orden,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //DETALLE DE UNA COMPRA CON VIEW
}
