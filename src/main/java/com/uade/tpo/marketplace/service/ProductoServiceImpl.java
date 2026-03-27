package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Producto;
import com.uade.tpo.marketplace.entity.dto.ProductoRequest;
import com.uade.tpo.marketplace.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long productoId) {
        return productoRepository.findById(productoId);
    }

    public Producto createProducto(ProductoRequest request) {
        return productoRepository.save(new Producto(request.getNombre(), request.getDescripcion(), request.getPrecio(), request.getCategoriaId()));
    }

    public Optional<Producto> updateProducto(Long productoId, ProductoRequest request) {
        return productoRepository.findById(productoId).map(producto -> {
            if (request.getNombre() != null) producto.setNombre(request.getNombre());
            if (request.getDescripcion() != null) producto.setDescripcion(request.getDescripcion());
            if (request.getPrecio() != null) producto.setPrecio(request.getPrecio());
            if (request.getCategoriaId() != null) producto.setCategoriaId(request.getCategoriaId());
            if (request.getHabilitado() != null) producto.setHabilitado(request.getHabilitado());
            return productoRepository.save(producto);
        });
    }

}
