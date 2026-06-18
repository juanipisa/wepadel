package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;
import com.uade.tpo.wepadel.exceptions.ProductoInvalidoException;
import com.uade.tpo.wepadel.exceptions.ProductoNotFoundException;
import com.uade.tpo.wepadel.exceptions.ProductoReferenciadoException;
import com.uade.tpo.wepadel.repository.CarritoItemRepository;
import com.uade.tpo.wepadel.repository.DescuentoRepository;
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

    public List<Producto> getProductos() {
        return productoRepository.findByEstaHabilitadoTrue();
    }

    public Producto getProductoById(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(ProductoNotFoundException::new);
    }

    public Producto createProducto(ProductoRequest request) {

        Producto producto = productoRepository.save(
                new Producto(request.getNombre(), request.getDescripcion(), request.getPrecio(), request.getCategoria())
        );

        // Inicializar stock en cero automáticamente al crear el producto
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

    // Borrado físico: solo si el producto no está referenciado en ninguna orden.
    // Las referencias "vivas" (carrito, descuentos, imágenes, stock) se limpian antes de borrar.
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