package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;

public interface ProductoService {
    List<Producto> getProductos();
    Producto getProductoById(Long productoId);
    Producto createProducto(ProductoRequest request);
    Optional<Producto> updateProducto(Long productoId, ProductoRequest request);
}