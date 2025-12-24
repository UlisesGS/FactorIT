package com.carrito.factorit.unit.services;

import com.carrito.factorit.enums.CompraOrdenFiltro;
import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.exceptions.ResourceNotFoundException;
import com.carrito.factorit.mapper.compra.CompraMapper;
import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.compra.Compra;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import com.carrito.factorit.models.persistence.compra.dto.PageCompraResponseDto;
import com.carrito.factorit.models.persistence.descuentoAplicado.DescuentoAplicado;
import com.carrito.factorit.repositories.CompraRepository;
import com.carrito.factorit.services.cliente.ClienteService;
import com.carrito.factorit.services.compra.CompraServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompraServiceImplTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private CompraMapper compraMapper;

    @InjectMocks
    private CompraServiceImpl compraService;

    private Compra crearCompra() {
        Compra compra = new Compra();
        compra.setId(1L);
        compra.setFechaCierre(LocalDateTime.now());
        return compra;
    }

    private CompraResponseDto crearCompraResponseDto() {
        return new CompraResponseDto();
    }

    private Pageable pageable() {
        return PageRequest.of(0, 10);
    }

    @Nested
    class ListAllProduct {

        @Test
        void ResourceNotFoundException_cliente_no_existe() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(false);

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> compraService.listAllProduct(
                            "123",
                            null,
                            null,
                            null,
                            pageable()
                    )
            );

            verify(compraRepository, never()).findByCarrito_Cliente_Dni(any(), any());
        }

        @Test
        void IllegalArgumentException_desde_mayor_que_hasta() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(true);

            LocalDate desde = LocalDate.of(2024, 10, 10);
            LocalDate hasta = LocalDate.of(2024, 10, 1);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> compraService.listAllProduct(
                            "123",
                            desde,
                            hasta,
                            null,
                            pageable()
                    )
            );
        }

        @Test
        void desde_con_hasta_null_usa_fecha_actual() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(true);

            LocalDate desde = LocalDate.now().minusDays(5);

            Page<Compra> page = new PageImpl<>(List.of(crearCompra()));

            when(compraRepository.findByCarrito_Cliente_DniAndFechaCierreBetween(
                    anyString(),
                    any(),
                    any(),
                    any()
            )).thenReturn(page);

            when(compraMapper.toResponse(any()))
                    .thenReturn(crearCompraResponseDto());

            PageCompraResponseDto response = compraService.listAllProduct(
                    "123",
                    desde,
                    null,
                    null,
                    pageable()
            );

            assertEquals(1, response.getContent().size());
            verify(compraRepository).findByCarrito_Cliente_DniAndFechaCierreBetween(
                    eq("123"),
                    eq(desde.atStartOfDay()),
                    any(),
                    any()
            );
        }

        @Test
        void sin_fechas_busca_por_dni() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(true);

            Page<Compra> page = new PageImpl<>(List.of(crearCompra()));

            when(compraRepository.findByCarrito_Cliente_Dni(anyString(), any()))
                    .thenReturn(page);

            when(compraMapper.toResponse(any()))
                    .thenReturn(crearCompraResponseDto());

            PageCompraResponseDto response = compraService.listAllProduct(
                    "123",
                    null,
                    null,
                    null,
                    pageable()
            );

            assertEquals(1, response.getContent().size());

            verify(compraRepository).findByCarrito_Cliente_Dni(
                    eq("123"),
                    any(Pageable.class)
            );
        }

        @Test
        void con_desde_y_hasta_busca_por_rango() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(true);

            LocalDate desde = LocalDate.of(2024, 10, 1);
            LocalDate hasta = LocalDate.of(2024, 10, 31);

            Page<Compra> page = new PageImpl<>(List.of(crearCompra()));

            when(compraRepository.findByCarrito_Cliente_DniAndFechaCierreBetween(
                    anyString(),
                    any(),
                    any(),
                    any()
            )).thenReturn(page);

            when(compraMapper.toResponse(any()))
                    .thenReturn(crearCompraResponseDto());

            PageCompraResponseDto response = compraService.listAllProduct(
                    "123",
                    desde,
                    hasta,
                    null,
                    pageable()
            );

            assertEquals(1, response.getContent().size());

            verify(compraRepository).findByCarrito_Cliente_DniAndFechaCierreBetween(
                    eq("123"),
                    eq(desde.atStartOfDay()),
                    eq(hasta.atTime(23, 59, 59)),
                    any(Pageable.class)
            );
        }

        @Test
        void aplica_orden_en_pageable() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(true);

            Page<Compra> page = new PageImpl<>(List.of(crearCompra()));

            when(compraRepository.findByCarrito_Cliente_Dni(anyString(), any()))
                    .thenReturn(page);

            when(compraMapper.toResponse(any()))
                    .thenReturn(crearCompraResponseDto());

            compraService.listAllProduct(
                    "123",
                    null,
                    null,
                    CompraOrdenFiltro.FECHA,
                    pageable()
            );

            verify(compraRepository).findByCarrito_Cliente_Dni(
                    eq("123"),
                    argThat(p -> p.getSort().isSorted())
            );
        }
    }


    @Nested
    class CrearCompra {

        @Test
        void crea_compra_y_asocia_descuentos() {
            Carrito carrito = new Carrito();

            ResultadoDescuento resultado = new ResultadoDescuento();
            resultado.setTotalFinal(BigDecimal.valueOf(2500));

            DescuentoAplicado d1 = new DescuentoAplicado();
            d1.setTipo(TipoDescuento.VIP);
            d1.setMonto(BigDecimal.valueOf(500));

            resultado.agregarDescuento(d1.getTipo(), d1.getMonto());

            CompraResponseDto responseDto = new CompraResponseDto();

            when(compraMapper.toResponse(any(Compra.class)))
                    .thenReturn(responseDto);

            CompraResponseDto result = compraService.crearCompra(carrito, resultado);

            assertNotNull(result);

            verify(compraRepository).save(any(Compra.class));
            verify(compraMapper).toResponse(any(Compra.class));

            verify(compraRepository).save(argThat(compra -> {
                assertEquals(BigDecimal.valueOf(2500), compra.getTotalPago());
                assertEquals(1, compra.getDescuentos().size());

                DescuentoAplicado descuento = compra.getDescuentos().get(0);
                assertEquals(compra, descuento.getCompra());

                return true;
            }));
        }

        @Test
        void sin_descuentos_funciona_correctamente() {
            Carrito carrito = new Carrito();

            ResultadoDescuento resultado = new ResultadoDescuento();
            resultado.setTotalFinal(BigDecimal.valueOf(1000));

            when(compraMapper.toResponse(any()))
                    .thenReturn(new CompraResponseDto());

            CompraResponseDto response = compraService.crearCompra(carrito, resultado);

            assertNotNull(response);

            verify(compraRepository).save(argThat(compra -> {
                assertEquals(BigDecimal.valueOf(1000), compra.getTotalPago());
                assertTrue(compra.getDescuentos().isEmpty());
                return true;
            }));
        }
    }

    @Nested
    class EsClienteVip {

        @Test
        void total_mes_null_devuelve_false() {
            LocalDateTime fecha = LocalDateTime.now();

            when(compraRepository.sumTotalByClienteAndFechaBetween(
                    anyString(), any(), any()
            )).thenReturn(null);

            boolean esVip = compraService.esClienteVip("123", fecha);

            assertFalse(esVip);
        }


        @Test
        void total_mes_menor_o_igual_a_5000_devuelve_false() {
            LocalDateTime fecha = LocalDateTime.now();

            when(compraRepository.sumTotalByClienteAndFechaBetween(
                    anyString(), any(), any()
            )).thenReturn(BigDecimal.valueOf(5000));

            boolean esVip = compraService.esClienteVip("123", fecha);

            assertFalse(esVip);
        }

        @Test
        void ya_uso_descuento_vip_devuelve_false() {
            LocalDateTime fecha = LocalDateTime.now();

            when(compraRepository.sumTotalByClienteAndFechaBetween(
                    anyString(), any(), any()
            )).thenReturn(BigDecimal.valueOf(6000));

            when(compraRepository.existsDescuentoUsadoEnMes(
                    anyString(),
                    eq(TipoDescuento.VIP),
                    any(),
                    any()
            )).thenReturn(true);

            boolean esVip = compraService.esClienteVip("123", fecha);

            assertFalse(esVip);
        }

        @Test
        void total_mayor_5000_y_no_uso_descuento_vip_devuelve_true() {
            LocalDateTime fecha = LocalDateTime.now();

            when(compraRepository.sumTotalByClienteAndFechaBetween(
                    anyString(), any(), any()
            )).thenReturn(BigDecimal.valueOf(8000));

            when(compraRepository.existsDescuentoUsadoEnMes(
                    anyString(),
                    eq(TipoDescuento.VIP),
                    any(),
                    any()
            )).thenReturn(false);

            boolean esVip = compraService.esClienteVip("123", fecha);

            assertTrue(esVip);
        }
    }
}
