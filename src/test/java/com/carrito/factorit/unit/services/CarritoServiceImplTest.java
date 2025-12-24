package com.carrito.factorit.unit.services;

import com.carrito.factorit.enums.CarritoStatus;
import com.carrito.factorit.exceptions.ConflictException;
import com.carrito.factorit.exceptions.ResourceNotFoundException;
import com.carrito.factorit.mapper.carrito.CarritoMapper;
import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.carrito.dto.*;
import com.carrito.factorit.models.persistence.cliente.Cliente;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import com.carrito.factorit.models.persistence.producto.Producto;
import com.carrito.factorit.models.persistence.productoCarrito.ProductoCarrito;
import com.carrito.factorit.repositories.CarritoRepository;
import com.carrito.factorit.services.carrito.CarritoServiceImpl;
import com.carrito.factorit.services.cliente.ClienteService;
import com.carrito.factorit.services.compra.CompraService;
import com.carrito.factorit.services.descuento.DescuentoService;
import com.carrito.factorit.services.producto.ProductoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceImplTest {

    @Mock
    CarritoRepository carritoRepository;

    @Mock
    ClienteService clienteService;

    @Mock
    CarritoMapper carritoMapper;

    @Mock
    ProductoService productoService;

    @Mock
    DescuentoService descuentoService;

    @Mock
    CompraService compraService;

    @InjectMocks
    CarritoServiceImpl carritoService;

    private Cliente cliente() {
        Cliente c = new Cliente();
        c.setDni("123");
        return c;
    }

    private Carrito carritoAbierto() {
        Carrito c = new Carrito();
        c.setId(1L);
        c.setEstado(CarritoStatus.ABIERTO);
        c.setProductos(new ArrayList<>());
        return c;
    }

    private Producto producto(Long id, BigDecimal precio, int stock) {
        Producto p = new Producto();
        p.setId(id);
        p.setPrecio(precio);
        p.setStock(stock);
        return p;
    }

    @Nested
    class CreateCarrito {

        @Test
        void crea_carrito_correctamente() {
            CreateCarritoRequestDto dto = new CreateCarritoRequestDto("123", false);

            when(clienteService.findByDni("123")).thenReturn(cliente());
            when(carritoRepository.findByCliente_DniAndEstado("123", CarritoStatus.ABIERTO))
                    .thenReturn(Optional.empty());
            when(carritoMapper.toResponse(any()))
                    .thenReturn(new CreateCarritoResponseDto());

            CreateCarritoResponseDto response = carritoService.createCarrito(dto);

            assertNotNull(response);
            verify(carritoRepository).save(any(Carrito.class));
        }

        @Test
        void ConflictException_si_cliente_tiene_carrito_abierto() {
            CreateCarritoRequestDto dto = new CreateCarritoRequestDto("123", false);

            when(clienteService.findByDni("123")).thenReturn(cliente());
            when(carritoRepository.findByCliente_DniAndEstado("123", CarritoStatus.ABIERTO))
                    .thenReturn(Optional.of(carritoAbierto()));

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.createCarrito(dto)
            );
        }
    }

    @Nested
    class DeleteCarrito {

        @Test
        void cancela_carrito_abierto() {
            Carrito carrito = carritoAbierto();

            when(clienteService.clienteExiste("123")).thenReturn(true);
            when(carritoRepository.findByCliente_DniAndEstado("123", CarritoStatus.ABIERTO))
                    .thenReturn(Optional.of(carrito));

            carritoService.deleteCarrito("123");

            assertEquals(CarritoStatus.CANCELADO, carrito.getEstado());
        }

        @Test
        void ResourceNotFoundException_si_cliente_no_existe() {
            when(clienteService.clienteExiste("123")).thenReturn(false);

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> carritoService.deleteCarrito("123")
            );
        }

        @Test
        void ConflictException_si_cliente_no_tiene_carrito_abierto() {
            when(clienteService.clienteExiste("123"))
                    .thenReturn(true);

            when(carritoRepository.findByCliente_DniAndEstado(
                    "123",
                    CarritoStatus.ABIERTO
            )).thenReturn(Optional.empty());

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.deleteCarrito("123")
            );
        }
    }

    @Nested
    class AddProducto {

        @Test
        void agrega_producto_nuevo_al_carrito() {
            Carrito carrito = carritoAbierto();
            Producto producto = producto(1L, BigDecimal.valueOf(100), 10);

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(productoService.findById(1L)).thenReturn(producto);

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 2);

            AddProductoResponseDto response =
                    carritoService.addProducto(1L, dto);

            assertEquals(BigDecimal.valueOf(200), response.getTotalPago());
            assertEquals(1, carrito.getProductos().size());
        }

        @Test
        void ConflictException_si_stock_insuficiente() {
            Carrito carrito = carritoAbierto();
            Producto producto = producto(1L, BigDecimal.valueOf(100), 1);

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(productoService.findById(1L)).thenReturn(producto);

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 5);

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.addProducto(1L, dto)
            );
        }
    }

    @Nested
    class DeleteProducto {

        @Test
        void elimina_producto_del_carrito() {
            Carrito carrito = carritoAbierto();
            Producto producto = producto(1L, BigDecimal.valueOf(100), 10);

            ProductoCarrito pc = new ProductoCarrito();
            pc.setProducto(producto);
            pc.setCantidad(2);
            carrito.getProductos().add(pc);

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(productoService.findById(1L)).thenReturn(producto);

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 2);

            carritoService.deleteProducto(1L, dto);

            assertTrue(carrito.getProductos().isEmpty());
        }

        @Test
        void ResourceNotFoundException_si_carrito_no_existe() {
            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.empty());

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 1);

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> carritoService.deleteProducto(1L, dto)
            );
        }

        @Test
        void ConflictException_si_carrito_no_esta_abierto() {
            Carrito carrito = carritoAbierto();
            carrito.setEstado(CarritoStatus.CERRADO);

            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.of(carrito));

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 1);

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.deleteProducto(1L, dto)
            );
        }

        @Test
        void ResourceNotFoundException_si_producto_no_esta_en_carrito() {
            Carrito carrito = carritoAbierto();
            Producto producto = producto(1L, BigDecimal.valueOf(100), 10);

            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.of(carrito));
            when(productoService.findById(1L))
                    .thenReturn(producto);

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 1);

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> carritoService.deleteProducto(1L, dto)
            );
        }

        @Test
        void ConflictException_si_cantidad_a_eliminar_supera_cantidad_actual() {
            Carrito carrito = carritoAbierto();
            Producto producto = producto(1L, BigDecimal.valueOf(100), 10);

            ProductoCarrito pc = new ProductoCarrito();
            pc.setProducto(producto);
            pc.setCantidad(2);
            carrito.getProductos().add(pc);

            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.of(carrito));
            when(productoService.findById(1L))
                    .thenReturn(producto);

            AddProductoRequestDto dto = new AddProductoRequestDto(1L, 5);

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.deleteProducto(1L, dto)
            );
        }
    }

    @Nested
    class EmptyCarrito {

        @Test
        void vaciar_carrito_con_productos() {
            Carrito carrito = carritoAbierto();
            carrito.getProductos().add(new ProductoCarrito());

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

            carritoService.emptyCarrito(1L);

            assertTrue(carrito.getProductos().isEmpty());
        }

        @Test
        void ConflictException_si_carrito_ya_esta_vacio() {
            Carrito carrito = carritoAbierto();

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.emptyCarrito(1L)
            );
        }

        @Test
        void ResourceNotFoundException_si_carrito_no_existe() {
            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.empty());


            assertThrows(
                    ResourceNotFoundException.class,
                    () -> carritoService.emptyCarrito(1L)
            );
        }

        @Test
        void ConflictException_si_carrito_no_esta_abierto() {
            Carrito carrito = carritoAbierto();
            carrito.setEstado(CarritoStatus.CERRADO);

            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.of(carrito));

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.emptyCarrito(1L)
            );
        }
    }

    @Nested
    class CheckStatusCarrito {

        @Test
        void devuelve_estado_y_total_productos() {
            Carrito carrito = carritoAbierto();

            Producto producto = producto(1L, BigDecimal.TEN, 10);
            producto.setNombre("Prod");
            producto.setCodigoProducto("P1");

            ProductoCarrito pc = new ProductoCarrito();
            pc.setProducto(producto);
            pc.setCantidad(3);
            carrito.getProductos().add(pc);

            when(carritoRepository.findById(1L))
                    .thenReturn(Optional.of(carrito));

            ListProductoCarritoResponseDto response =
                    carritoService.checkStatusCarrito(1L);

            assertEquals(3, response.getTotalProductos());
            assertEquals(CarritoStatus.ABIERTO, response.getEstado());
        }
    }

    @Nested
    class CloseCarrito {

        @Test
        void cierra_carrito_y_crea_compra() {
            Carrito carrito = carritoAbierto();
            carrito.getProductos().add(new ProductoCarrito());

            ResultadoDescuento resultado = new ResultadoDescuento();
            resultado.setTotalFinal(BigDecimal.valueOf(1000));

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
            when(descuentoService.calcularTotal(carrito)).thenReturn(resultado);
            when(compraService.crearCompra(eq(carrito), eq(resultado)))
                    .thenReturn(new CompraResponseDto());

            CompraResponseDto response = carritoService.closeCarrito(1L);

            assertEquals(CarritoStatus.CERRADO, carrito.getEstado());
            assertNotNull(response);
        }

        @Test
        void lanza_error_si_carrito_esta_vacio() {
            Carrito carrito = carritoAbierto();

            when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

            assertThrows(
                    ConflictException.class,
                    () -> carritoService.closeCarrito(1L)
            );
        }
    }
}
