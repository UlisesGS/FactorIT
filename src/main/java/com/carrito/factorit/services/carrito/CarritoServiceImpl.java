package com.carrito.factorit.services.carrito;

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
import com.carrito.factorit.models.persistence.producto.dto.ProductoResponseDto;
import com.carrito.factorit.models.persistence.productoCarrito.ProductoCarrito;
import com.carrito.factorit.models.persistence.productoCarrito.dto.ProductoCarritoResponseDto;
import com.carrito.factorit.repositories.CarritoRepository;
import com.carrito.factorit.services.producto.ProductoService;
import com.carrito.factorit.services.cliente.ClienteService;
import com.carrito.factorit.services.compra.CompraService;
import com.carrito.factorit.services.descuento.DescuentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService{

    private final CarritoRepository carritoRepository;
    private final ClienteService clienteService;
    private final CarritoMapper carritoMapper;
    private final ProductoService productoService;
    private final DescuentoService descuentoService;
    private final CompraService compraService;


    @Override
    @Transactional
    public CreateCarritoResponseDto createCarrito(CreateCarritoRequestDto createCarritoRequestDto) {
        Cliente cliente = clienteService.findByDni(createCarritoRequestDto.getClienteDni());

        carritoRepository.findByCliente_DniAndEstado(createCarritoRequestDto.getClienteDni(), CarritoStatus.ABIERTO)
                .ifPresent(c -> {
                    throw new ConflictException("El cliente ya tiene un carrito abierto");
                });

        Carrito carrito = new Carrito(cliente, createCarritoRequestDto.getEsEspecial());

        carritoRepository.save(carrito);

        return carritoMapper.toResponse(carrito);
    }


    @Override
    @Transactional
    public void deleteCarrito(String dni) {

        if (!clienteService.clienteExiste(dni)){
            throw new ResourceNotFoundException("Error en la busqueda del cliente");
        }

        Carrito carrito = carritoRepository
                .findByCliente_DniAndEstado(dni, CarritoStatus.ABIERTO)
                .orElseThrow(() ->
                        new ConflictException("El cliente no tiene un carrito abierto")
                );
        carrito.setFechaCierre(LocalDateTime.now());
        carrito.setEstado(CarritoStatus.CANCELADO);
    }


    @Override
    @Transactional
    public AddProductoResponseDto addProducto(Long carritoId, AddProductoRequestDto addProductoRequestDto) {
        Carrito carrito = getCarritoAbierto(carritoId);

        Producto producto = productoService.findById(addProductoRequestDto.getProductoId());

        if (producto.getStock() < addProductoRequestDto.getCantidad()) {
            throw new ConflictException("Stock insuficiente para el producto");
        }

        Optional<ProductoCarrito> existente = carrito.getProductos()
                .stream()
                .filter(p -> p.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            ProductoCarrito pc = existente.get();
            pc.setCantidad(pc.getCantidad() + addProductoRequestDto.getCantidad());
        } else {
            ProductoCarrito nuevo = new ProductoCarrito();
            nuevo.setCarrito(carrito);
            nuevo.setProducto(producto);
            nuevo.setCantidad(addProductoRequestDto.getCantidad());
            carrito.getProductos().add(nuevo);
        }

        recalcularTotal(carrito);

        carritoRepository.save(carrito);

        return new AddProductoResponseDto(
                carrito.getId(),
                carrito.getTotalPago()
        );
    }


    @Override
    @Transactional
    public AddProductoResponseDto deleteProducto(Long carritoId, AddProductoRequestDto addProductoRequestDto) {
        Carrito carrito = getCarritoAbierto(carritoId);

        productoService.findById(addProductoRequestDto.getProductoId());


        ProductoCarrito productoCarrito = carrito.getProductos().stream()
                .filter(p -> p.getProducto().getId().equals(addProductoRequestDto.getProductoId()))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("El producto no esta en el carrito")
                );

        int nuevaCantidad = productoCarrito.getCantidad() - addProductoRequestDto.getCantidad();

        if (nuevaCantidad < 0) {
            throw new ConflictException("La cantidad a eliminar supera la cantidad actual");
        }

        if (nuevaCantidad == 0) {
            carrito.getProductos().remove(productoCarrito);
        } else {
            productoCarrito.setCantidad(nuevaCantidad);
        }

        recalcularTotal(carrito);


        return new AddProductoResponseDto(
                carrito.getId(),
                carrito.getTotalPago()
        );
    }


    @Override
    @Transactional
    public void emptyCarrito(Long carritoId) {
        Carrito carrito = getCarritoAbierto(carritoId);

        if (carrito.getProductos().isEmpty()){
            throw new ConflictException("No se puede vaciar, el carrito ya esta vacio");
        }

        carrito.getProductos().clear();
        recalcularTotal(carrito);
    }


    @Override
    @Transactional(readOnly = true)
    public ListProductoCarritoResponseDto checkStatusCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        List<ProductoCarritoResponseDto> productos = carrito.getProductos().stream()
                .map(p -> new ProductoCarritoResponseDto(
                        List.of(new ProductoResponseDto(p.getProducto().getNombre(), p.getProducto().getCodigoProducto())),
                        p.getCantidad()
                ))
                .collect(Collectors.toList());

        int totalProductos = carrito.getProductos().stream()
                .mapToInt(ProductoCarrito::getCantidad)
                .sum();

        return new ListProductoCarritoResponseDto(productos, carrito.getEstado(), totalProductos);
    }


    @Override
    @Transactional
    public CompraResponseDto closeCarrito(Long carritoId) {
        Carrito carrito = getCarritoAbierto(carritoId);

        if (carrito.getProductos().isEmpty()) {
            throw new ConflictException("No se puede cerrar un carrito vacio");
        }

        carrito.setFechaCierre(LocalDateTime.now());

        ResultadoDescuento resultado = descuentoService.calcularTotal(carrito);

        carrito.setEstado(CarritoStatus.CERRADO);
        carritoRepository.save(carrito);
        return compraService.crearCompra(carrito, resultado);
    }


    private Carrito getCarritoAbierto(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        if (carrito.getEstado() != CarritoStatus.ABIERTO) {
            throw new ConflictException("El carrito no esta ABIERTO");
        }

        return carrito;
    }


    private void recalcularTotal(Carrito carrito) {
        BigDecimal total = carrito.getProductos().stream()
                .map(p -> p.getProducto().getPrecio()
                        .multiply(BigDecimal.valueOf(p.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrito.setTotalPago(total);
    }

}
