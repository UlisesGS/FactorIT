package com.carrito.factorit.services.descuento;

import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.productoCarrito.ProductoCarrito;
import com.carrito.factorit.services.compra.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DescuentoServiceImpl implements DescuentoService {

    private final CompraService compraService;

    @Override
    public ResultadoDescuento calcularTotal(Carrito carrito) {
        ResultadoDescuento resultado = new ResultadoDescuento();

        BigDecimal subtotal = calcularSubtotalSinDescuentos(carrito);
        resultado.setSubtotal(subtotal);

        BigDecimal desc4x3 = descuento4x3(carrito);
        if (desc4x3.compareTo(BigDecimal.ZERO) > 0) {
            resultado.agregarDescuento(TipoDescuento.PROMO_4X3, desc4x3);
        }

        BigDecimal descCantidad = descuentoPorCantidad(carrito);
        if (descCantidad.compareTo(BigDecimal.ZERO) > 0) {
            resultado.agregarDescuento(TipoDescuento.CANTIDAD, descCantidad);
        }

        BigDecimal totalParcial = subtotal
                .subtract(desc4x3)
                .subtract(descCantidad);

        if (aplicaVip(carrito, totalParcial)) {
            resultado.agregarDescuento(TipoDescuento.VIP, BigDecimal.valueOf(500));
            totalParcial = totalParcial.subtract(BigDecimal.valueOf(500));
        }

        resultado.setTotalFinal(totalParcial.max(BigDecimal.ZERO));
        return resultado;
    }

    private BigDecimal calcularSubtotalSinDescuentos(Carrito carrito) {
        return carrito.getProductos().stream()
                .map(p -> p.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(p.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private BigDecimal descuentoPorCantidad(Carrito carrito) {
        int totalProductos = carrito.getProductos().stream()
                .mapToInt(ProductoCarrito::getCantidad)
                .sum();

        if (totalProductos > 3) {
            return carrito.isEsEspecial()
                    ? BigDecimal.valueOf(150)
                    : BigDecimal.valueOf(100);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal descuento4x3(Carrito carrito) {
        return carrito.getProductos().stream()
                .map(p -> {
                    BigDecimal precio = p.getProducto().getPrecio();
                    int cantidad = p.getCantidad();
                    int gratis = cantidad / 4;

                    return precio.multiply(BigDecimal.valueOf(gratis));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean aplicaVip(Carrito carrito, BigDecimal total) {
        return total.compareTo(BigDecimal.valueOf(2000)) > 0
                && compraService.esClienteVip(
                carrito.getCliente().getDni(),
                carrito.getFechaCierre()
        );
    }
}
