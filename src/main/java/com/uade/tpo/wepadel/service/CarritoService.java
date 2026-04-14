package com.uade.tpo.wepadel.service;

import java.util.List;

import com.uade.tpo.wepadel.entity.Carrito;
import com.uade.tpo.wepadel.entity.CarritoItem;
import com.uade.tpo.wepadel.entity.dto.CarritoItemRequest;

public interface CarritoService {
    public Carrito getCarritoByUsuarioId(Long usuarioId);
    public Carrito createCarrito(Long usuarioId);
    public List<CarritoItem> getItems(Long usuarioId);
    public CarritoItem addItem(Long usuarioId, CarritoItemRequest request);
    public CarritoItem updateCantidad(Long usuarioId, Long productoId, int nuevaCantidad);
    public void removeItem(Long usuarioId, Long productoId);
    public void vaciarCarrito(Long usuarioId);
}
