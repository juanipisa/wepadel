package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;

public interface ProductoService {
    public List<Producto> getProductos();
    public Optional<Producto> getProductoById(Long productoId);
    public Producto createProducto(ProductoRequest request);
    public Optional<Producto> updateProducto(Long productoId, ProductoRequest request);
    //TODO: Debo filtrar productos por estaHabilitado en getProductos()?
}
