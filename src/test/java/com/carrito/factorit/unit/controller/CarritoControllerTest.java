package com.carrito.factorit.unit.controller;
import com.carrito.factorit.controllers.CarritoController;
import com.carrito.factorit.enums.CarritoStatus;
import com.carrito.factorit.models.persistence.carrito.dto.*;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import com.carrito.factorit.services.carrito.CarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(carritoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Nested
    class CheckStatusCarrito {

        @Test
        void checkStatusCarrito_ok() throws Exception {
            ListProductoCarritoResponseDto responseDto =
                    new ListProductoCarritoResponseDto(List.of(), CarritoStatus.ABIERTO, 0);

            when(carritoService.checkStatusCarrito(1L)).thenReturn(responseDto);

            mockMvc.perform(get("/api/carrito/consultar-estado/{carritoId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("ABIERTO"));
        }
    }

    @Nested
    class CreateCarrito {

        @Test
        void createCarrito_ok() throws Exception {
            CreateCarritoRequestDto requestDto = new CreateCarritoRequestDto("12345678", true);
            CreateCarritoResponseDto responseDto = new CreateCarritoResponseDto
                    (LocalDateTime.now(), true, CarritoStatus.ABIERTO, BigDecimal.ZERO);

            when(carritoService.createCarrito(any(CreateCarritoRequestDto.class))).thenReturn(responseDto);

            mockMvc.perform(post("/api/carrito/crear")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.esEspecial").value(true))
                    .andExpect(jsonPath("$.estado").value("ABIERTO"));;
        }
    }

    @Nested
    class CloseCarrito {

        @Test
        void closeCarrito_ok() throws Exception {
            CompraResponseDto responseDto = new CompraResponseDto(1L, LocalDateTime.now(), BigDecimal.valueOf(200), 1L);

            when(carritoService.closeCarrito(1L)).thenReturn(responseDto);

            mockMvc.perform(post("/api/carrito/cerrar/{carritoId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L));
        }
    }

    @Nested
    class DeleteCarrito {

        @Test
        void deleteCarrito_ok() throws Exception {
            mockMvc.perform(delete("/api/carrito/eliminar/{dni}", "12345678"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class AddProducto {

        @Test
        void addProducto_ok() throws Exception {
            AddProductoRequestDto requestDto = new AddProductoRequestDto(1L, 2);
            AddProductoResponseDto responseDto = new AddProductoResponseDto(1L, BigDecimal.valueOf(200));

            when(carritoService.addProducto(eq(1L), any(AddProductoRequestDto.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(post("/api/carrito/agregar-producto/{carritoId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.totalPago").value(200));
        }
    }

    @Nested
    class DeleteProducto {

        @Test
        void deleteProducto_ok() throws Exception {
            AddProductoRequestDto requestDto = new AddProductoRequestDto(1L, 1);
            AddProductoResponseDto responseDto = new AddProductoResponseDto(1L, BigDecimal.valueOf(100));

            when(carritoService.deleteProducto(eq(1L), any(AddProductoRequestDto.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(delete("/api/carrito/sacar-producto/{carritoId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.totalPago").value(100));
        }
    }

    @Nested
    class EmptyCarrito {

        @Test
        void emptyCarrito_ok() throws Exception {
            mockMvc.perform(delete("/api/carrito/vaciar/{carritoId}", 1L))
                    .andExpect(status().isNoContent());
        }
    }
}