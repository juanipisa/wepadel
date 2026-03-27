package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Producto;
import com.uade.tpo.marketplace.entity.dto.ProductoRequest;

public interface ProductoService {
    public List<Producto> getProductos();
    public Optional<Producto> getProductoById(Long productoId);
    public Producto createProducto(ProductoRequest request);
    public Optional<Producto> updateProducto(Long productoId, ProductoRequest request);
}
