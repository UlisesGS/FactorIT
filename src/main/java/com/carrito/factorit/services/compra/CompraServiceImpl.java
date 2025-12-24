package com.carrito.factorit.services.compra;

import com.carrito.factorit.enums.CompraOrdenFiltro;
import com.carrito.factorit.enums.TipoDescuento;
import com.carrito.factorit.exceptions.ResourceNotFoundException;
import com.carrito.factorit.mapper.compra.CompraMapper;
import com.carrito.factorit.models.domain.ResultadoDescuento;
import com.carrito.factorit.models.persistence.carrito.Carrito;
import com.carrito.factorit.models.persistence.compra.Compra;
import com.carrito.factorit.models.persistence.compra.dto.CompraResponseDto;
import com.carrito.factorit.models.persistence.compra.dto.PageCompraResponseDto;
import com.carrito.factorit.repositories.CompraRepository;
import com.carrito.factorit.services.cliente.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final ClienteService clienteService;
    private final CompraRepository compraRepository;
    private final CompraMapper compraMapper;


    @Override
    @Transactional(readOnly = true)
    public PageCompraResponseDto listAllProduct(String dni, LocalDate desde, LocalDate hasta, CompraOrdenFiltro orden, Pageable pageable) {

        if (!clienteService.clienteExiste(dni)){
            throw new ResourceNotFoundException("Error en la busqueda del cliente");
        }

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha 'desde' no puede ser mayor que 'hasta'");
        } else if (desde != null && hasta == null) {
            hasta=LocalDate.now();
        }

        CompraOrdenFiltro ordenFinal = orden != null ? orden : CompraOrdenFiltro.FECHA;

        Sort sort = CompraSortBuilder.build(ordenFinal);

        System.out.println(sort);
        Pageable pageableOrdenado = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<Compra> compras = this.buscarCompras(dni, desde, hasta, pageableOrdenado);

        List<CompraResponseDto> compraResponseDtos=compras.map(compraMapper::toResponse).getContent();

        return new PageCompraResponseDto(
                compraResponseDtos,
                compras.getNumber(),
                compras.getSize(),
                compras.getTotalElements(),
                compras.getTotalPages()
        );
    }


    @Override
    @Transactional
    public CompraResponseDto crearCompra(Carrito carrito, ResultadoDescuento resultado) {
        Compra compra = new Compra(carrito, resultado.getTotalFinal());

        resultado.getDescuentos().forEach(d -> {
            d.setCompra(compra);
            compra.getDescuentos().add(d);
        });

        compraRepository.save(compra);
        return compraMapper.toResponse(compra);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean esClienteVip(String dni, LocalDateTime fecha) {

        LocalDateTime inicioMes = fecha.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);

        BigDecimal totalMes = compraRepository
                .sumTotalByClienteAndFechaBetween(dni, inicioMes, fecha);

        if (totalMes == null || totalMes.compareTo(BigDecimal.valueOf(5000)) <= 0) {
            return false;
        }

        return !compraRepository.existsDescuentoUsadoEnMes(
                dni, TipoDescuento.VIP, inicioMes, finMes
        );
    }


    private Page<Compra> buscarCompras(String dni, LocalDate desde, LocalDate hasta, Pageable pageable){
        if (desde != null && hasta != null) {
            return compraRepository.findByCarrito_Cliente_DniAndFechaCierreBetween(
                    dni,
                    desde.atStartOfDay(),
                    hasta.atTime(23, 59, 59),
                    pageable
            );
        }
        return compraRepository.findByCarrito_Cliente_Dni(dni, pageable);
    }
}
