package com.carrito.factorit.services.compra;

import com.carrito.factorit.enums.CompraOrdenFiltro;
import org.springframework.data.domain.Sort;

public class CompraSortBuilder {
    private CompraSortBuilder() {}

    public static Sort build(CompraOrdenFiltro orden) {
        return switch (orden) {
            case FECHA -> Sort.by("fechaCierre");
            case MONTO -> Sort.by("totalPago");
            default -> Sort.unsorted();
        };
    }
}
