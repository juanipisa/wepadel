package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;
import com.uade.tpo.wepadel.exceptions.ProductoInvalidoException;
import com.uade.tpo.wepadel.exceptions.ProductoNotFoundException;
import com.uade.tpo.wepadel.repository.ProductoRepository;
import com.uade.tpo.wepadel.repository.StockRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<Producto> getProductos() {
        return productoRepository.findByEstaHabilitadoTrue();
    }

    public Producto getProductoById(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(ProductoNotFoundException::new);
    }

    public Producto createProducto(ProductoRequest request) {
        validarProducto(request);

        Producto producto = productoRepository.save(
                new Producto(request.getDescripcion(), request.getPrecio(), request.getCategoria())
        );

        // Inicializar stock en cero automáticamente al crear el producto
        stockRepository.save(new Stock(producto.getId(), 0));

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

    private void validarProducto(ProductoRequest request) {
        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new ProductoInvalidoException("La descripción no puede estar vacía");
        }
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductoInvalidoException("El precio debe ser mayor a cero");
        }
    }

}