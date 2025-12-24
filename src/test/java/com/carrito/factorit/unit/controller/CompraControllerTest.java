package com.carrito.factorit.unit.controller;

import com.carrito.factorit.controllers.CompraController;
import com.carrito.factorit.enums.CompraOrdenFiltro;
import com.carrito.factorit.models.persistence.compra.dto.PageCompraResponseDto;
import com.carrito.factorit.services.compra.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CompraControllerTest {

    @Mock
    private CompraService compraService;

    @InjectMocks
    private CompraController compraController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(compraController)
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                )
                .build();
    }

    @Test
    void listAllCompra_ok() throws Exception {
        PageCompraResponseDto response =
                new PageCompraResponseDto(List.of(), 0, 10, 0, 0);

        when(compraService.listAllProduct(
                eq("12345678"),
                any(),
                any(),
                eq(CompraOrdenFiltro.FECHA),
                any(Pageable.class)
        )).thenReturn(response);

        mockMvc.perform(
                        get("/api/compras/listar-compras/{dni}", "12345678")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk());

        verify(compraService).listAllProduct(
                eq("12345678"),
                isNull(),
                isNull(),
                eq(CompraOrdenFiltro.FECHA),
                any(Pageable.class)
        );
    }

    @Test
    void listAllCompra_con_fechas() throws Exception {
        PageCompraResponseDto response =
                new PageCompraResponseDto(List.of(), 0, 10, 0, 0);

        when(compraService.listAllProduct(
                eq("12345678"),
                eq(LocalDate.of(2025, 1, 1)),
                eq(LocalDate.of(2025, 1, 31)),
                eq(CompraOrdenFiltro.FECHA),
                any(Pageable.class)
        )).thenReturn(response);

        mockMvc.perform(
                        get("/api/compras/listar-compras/{dni}", "12345678")
                                .param("desde", "2025-01-01")
                                .param("hasta", "2025-01-31")
                )
                .andExpect(status().isOk());
    }

    @Test
    void listAllCompra_con_orden_monto() throws Exception {
        PageCompraResponseDto response =
                new PageCompraResponseDto(List.of(), 0, 10, 0, 0);

        when(compraService.listAllProduct(
                eq("12345678"),
                any(),
                any(),
                eq(CompraOrdenFiltro.MONTO),
                any(Pageable.class)
        )).thenReturn(response);

        mockMvc.perform(
                        get("/api/compras/listar-compras/{dni}", "12345678")
                                .param("orden", "MONTO")
                )
                .andExpect(status().isOk());
    }
}