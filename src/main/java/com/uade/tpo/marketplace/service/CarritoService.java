package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Carrito;
import com.uade.tpo.marketplace.entity.CarritoItem;

public interface CarritoService {
    public Optional<Carrito> getCarritoById(Long carritoId);
    public Carrito createCarrito(Long usuarioId);
    public List<CarritoItem> getItems(Long carritoId);
    public CarritoItem addItem(Long carritoId, Long productoId, int cantidad);
    public boolean removeItem(Long carritoId, Long productoId);
    public void resetCarrito(Long carritoId);
}
