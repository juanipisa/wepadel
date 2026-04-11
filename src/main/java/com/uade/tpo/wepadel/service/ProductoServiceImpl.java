package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;
import com.uade.tpo.wepadel.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getProductos() {
        //TODO: Filtrar solo productos habilitados (estaHabilitado = true)
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long productoId) {
        return productoRepository.findById(productoId);
    }

    public Producto createProducto(ProductoRequest request) {
        //TODO: Validar que el usuario es ADMIN
        return productoRepository.save(new Producto(request.getDescripcion(), request.getPrecio(), 
                request.getCategoria()));
    }

    public Optional<Producto> updateProducto(Long productoId, ProductoRequest request) {
        //TODO: Validar que el usuario es ADMIN
        return productoRepository.findById(productoId).map(producto -> {
            if (request.getDescripcion() != null) producto.setDescripcion(request.getDescripcion());
            if (request.getPrecio() != null) producto.setPrecio(request.getPrecio());
            if (request.getEstaHabilitado() != null) producto.setEstaHabilitado(request.getEstaHabilitado());
            // Nota: categoría NO se puede cambiar según el DER
            return productoRepository.save(producto);
        });
    }

}
