package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.DescuentoResponse;
import com.uade.tpo.wepadel.entity.dto.ImagenCatalogoResponse;
import com.uade.tpo.wepadel.entity.dto.ProductoEnrichedResponse;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;
import com.uade.tpo.wepadel.exceptions.ProductoInvalidoException;
import com.uade.tpo.wepadel.exceptions.ProductoNotFoundException;
import com.uade.tpo.wepadel.exceptions.ProductoReferenciadoException;
import com.uade.tpo.wepadel.repository.CarritoItemRepository;
import com.uade.tpo.wepadel.repository.DescuentoRepository;
import com.uade.tpo.wepadel.repository.ImagenMetadataProjection;
import com.uade.tpo.wepadel.repository.ImagenRepository;
import com.uade.tpo.wepadel.repository.OrdenItemRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;
import com.uade.tpo.wepadel.repository.StockRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrdenItemRepository ordenItemRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEnrichedResponse> getProductosEnriched(boolean includeDeshabilitados) {
        List<Producto> productos = includeDeshabilitados
                ? productoRepository.findAll()
                : productoRepository.findByEstaHabilitadoTrue();

        if (productos.isEmpty()) {
            return List.of();
        }

        List<Long> productoIds = productos.stream().map(Producto::getId).toList();

        Map<Long, Integer> stockPorProducto = stockRepository.findByProductoIdIn(productoIds).stream()
                .collect(Collectors.toMap(s -> s.getProducto().getId(), Stock::getCantidad));

        Map<Long, ImagenCatalogoResponse> imagenPrincipalPorProducto = new HashMap<>();
        for (ImagenMetadataProjection metadata : imagenRepository.findMetadataByProductoIdIn(productoIds)) {
            imagenPrincipalPorProducto.putIfAbsent(
                    metadata.getProductoId(),
                    toImagenCatalogoResponse(metadata));
        }

        Map<Long, List<DescuentoResponse>> descuentosPorProducto = descuentoRepository
                .findByProductoIdIn(productoIds).stream()
                .collect(Collectors.groupingBy(
                        d -> d.getProducto().getId(),
                        Collectors.mapping(this::toDescuentoResponse, Collectors.toList())));

        return productos.stream()
                .map(p -> ProductoEnrichedResponse.builder()
                        .id(p.getId())
                        .nombre(p.getNombre())
                        .descripcion(p.getDescripcion())
                        .precio(p.getPrecio())
                        .categoria(p.getCategoria())
                        .estaHabilitado(p.getEstaHabilitado())
                        .stock(stockPorProducto.getOrDefault(p.getId(), 0))
                        .imagenPrincipal(imagenPrincipalPorProducto.get(p.getId()))
                        .descuentos(descuentosPorProducto.getOrDefault(p.getId(), List.of()))
                        .build())
                .toList();
    }

    private ImagenCatalogoResponse toImagenCatalogoResponse(ImagenMetadataProjection metadata) {
        return ImagenCatalogoResponse.builder()
                .id(metadata.getId())
                .nombre(metadata.getNombre())
                .url(ImagenService.urlArchivo(metadata.getId()))
                .build();
    }

    private DescuentoResponse toDescuentoResponse(Descuento descuento) {
        return DescuentoResponse.builder()
                .id(descuento.getId())
                .porcentaje(descuento.getPorcentaje())
                .fechaInicio(descuento.getFechaInicio())
                .fechaFin(descuento.getFechaFin())
                .activo(descuento.getActivo())
                .build();
    }

    public Producto getProductoById(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(ProductoNotFoundException::new);
    }

    public Producto createProducto(ProductoRequest request) {
        Producto producto = productoRepository.save(
                new Producto(request.getNombre(), request.getDescripcion(), request.getPrecio(), request.getCategoria())
        );
        if (request.getEstaHabilitado() != null) {
            producto.setEstaHabilitado(request.getEstaHabilitado());
            producto = productoRepository.save(producto);
        }

        stockRepository.save(new Stock(producto, 0));

        return producto;
    }

    public Optional<Producto> updateProducto(Long productoId, ProductoRequest request) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(ProductoNotFoundException::new);

        if (request.getDescripcion() != null) {
            if (request.getDescripcion().isBlank()) {
                throw new ProductoInvalidoException("La descripción no puede estar vacía");
            }
            producto.setDescripcion(request.getDescripcion());
        }

        if (request.getNombre() != null) {
            if (request.getNombre().isBlank()) {
                throw new ProductoInvalidoException("El nombre no puede estar vacío");
            }
            producto.setNombre(request.getNombre());
        }

        if (request.getPrecio() != null) {
            if (request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ProductoInvalidoException("El precio debe ser mayor a cero");
            }
            producto.setPrecio(request.getPrecio());
        }

        if (request.getEstaHabilitado() != null) {
            producto.setEstaHabilitado(request.getEstaHabilitado());
        }

        return Optional.of(productoRepository.save(producto));
    }

    @Transactional
    public Producto deleteProducto(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(ProductoNotFoundException::new);

        if (ordenItemRepository.existsByProductoId(productoId)) {
            throw new ProductoReferenciadoException(
                    "No se puede eliminar el producto porque está asociado a una o más órdenes");
        }

        carritoItemRepository.deleteByProductoId(productoId);
        descuentoRepository.deleteByProductoId(productoId);
        imagenRepository.deleteByProductoId(productoId);
        stockRepository.deleteByProductoId(productoId);

        productoRepository.delete(producto);

        return producto;
    }

}
