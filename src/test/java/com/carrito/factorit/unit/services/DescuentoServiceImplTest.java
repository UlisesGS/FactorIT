package com.carrito.factorit.unit.services;

import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.cliente.Cliente;
import com.carrito.factorit.models.persistence.producto.Producto;
import com.carrito.factorit.models.persistence.productoCarrito.ProductoCarrito;
import com.carrito.factorit.services.compra.CompraService;
import com.carrito.factorit.services.descuento.DescuentoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescuentoServiceImplTest {

    @Mock
    private CompraService compraService;

    @InjectMocks
    private DescuentoServiceImpl descuentoService;


    private Carrito crearCarrito(
            boolean esEspecial,
            BigDecimal precioProducto,
            int cantidad
    ) {
        Producto producto = new Producto();
        producto.setPrecio(precioProducto);

        ProductoCarrito pc = new ProductoCarrito();
        pc.setProducto(producto);
        pc.setCantidad(cantidad);

        Cliente cliente = new Cliente();
        cliente.setDni("12345678");

        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setEsEspecial(esEspecial);
        carrito.setFechaCierre(LocalDateTime.now());
        carrito.setProductos(List.of(pc));

        return carrito;
    }


    @Test
    void calcularTotal_aplica_cantidad_4x3_correctamente() {
        Carrito carrito = crearCarrito(
                false,
                BigDecimal.valueOf(100),
                4
        );

        ResultadoDescuento resultado = descuentoService.calcularTotal(carrito);

        assertEquals(BigDecimal.valueOf(400), resultado.getSubtotal());
        assertEquals(BigDecimal.valueOf(200), resultado.getTotalFinal());

        assertTrue(
                resultado.getDescuentos().stream()
                        .anyMatch(d ->
                                d.getTipo() == TipoDescuento.PROMO_4X3 &&
                                        d.getMonto().compareTo(BigDecimal.valueOf(100)) == 0
                        )
        );
    }

    @Test
    void calcularTotal_aplica_cantidad_4x3_dos_veces_correctamente() {
        Carrito carrito = crearCarrito(
                false,
                BigDecimal.valueOf(100),
                8
        );

        ResultadoDescuento resultado = descuentoService.calcularTotal(carrito);

        assertEquals(BigDecimal.valueOf(800), resultado.getSubtotal());
        assertEquals(BigDecimal.valueOf(500), resultado.getTotalFinal());

        assertTrue(
                resultado.getDescuentos().stream()
                        .anyMatch(d ->
                                d.getTipo() == TipoDescuento.PROMO_4X3 &&
                                        d.getMonto().compareTo(BigDecimal.valueOf(200)) == 0
                        )
        );
    }



    @Test
    void calcularTotal_aplica_vip() {
        Carrito carrito = crearCarrito(
                false,
                BigDecimal.valueOf(3000),
                1
        );

        when(compraService.esClienteVip(anyString(), any()))
                .thenReturn(true);

        ResultadoDescuento resultado = descuentoService.calcularTotal(carrito);

        assertEquals(BigDecimal.valueOf(3000), resultado.getSubtotal());
        assertEquals(BigDecimal.valueOf(2500), resultado.getTotalFinal());

        assertTrue(
                resultado.getDescuentos().stream()
                        .anyMatch(d ->
                                d.getTipo() == TipoDescuento.VIP &&
                                        d.getMonto().compareTo(BigDecimal.valueOf(500)) == 0
                        )
        );
    }

    @Test
    void calcularTotal_aplica_vip_cantidad_4x3() {
        Carrito carrito = crearCarrito(
                false,
                BigDecimal.valueOf(1000),
                5
        );

        when(compraService.esClienteVip(anyString(), any()))
                .thenReturn(true);

        ResultadoDescuento resultado = descuentoService.calcularTotal(carrito);

        assertEquals(BigDecimal.valueOf(5000), resultado.getSubtotal());
        assertEquals(BigDecimal.valueOf(3400), resultado.getTotalFinal());

        assertTrue(
                resultado.getDescuentos().stream()
                        .anyMatch(d ->
                                d.getTipo() == TipoDescuento.VIP &&
                                        d.getMonto().compareTo(BigDecimal.valueOf(500)) == 0
                        )
        );
    }


    @Test
    void calcularTotal_aplica_vip_cantidad_4x3_especial() {
        Carrito carrito = crearCarrito(
                true,
                BigDecimal.valueOf(1000),
                5
        );

        when(compraService.esClienteVip(anyString(), any()))
                .thenReturn(true);

        ResultadoDescuento resultado = descuentoService.calcularTotal(carrito);

        assertEquals(BigDecimal.valueOf(5000), resultado.getSubtotal());
        assertEquals(BigDecimal.valueOf(3350), resultado.getTotalFinal());

        assertTrue(
                resultado.getDescuentos().stream()
                        .anyMatch(d ->
                                d.getTipo() == TipoDescuento.VIP &&
                                        d.getMonto().compareTo(BigDecimal.valueOf(500)) == 0
                        )
        );
    }
}
