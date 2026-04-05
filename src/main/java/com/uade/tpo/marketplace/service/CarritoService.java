package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Carrito;
import com.uade.tpo.marketplace.entity.CarritoItem;
import com.uade.tpo.marketplace.entity.dto.CarritoItemRequest;

public interface CarritoService {
    public Optional<Carrito> getCarritoByUsuarioId(Long usuarioId);
    public Carrito createCarrito(Long usuarioId);
    public List<CarritoItem> getItems(Long usuarioId);
    public CarritoItem addItem(Long usuarioId, CarritoItemRequest request);
    public boolean removeItem(Long usuarioId, Long productoId);
    public void deleteCarrito(Long usuarioId);
    //TODO: Se borra el carrito solo cuando se confirma orden o también si el usuario abandona?
    //TODO: Quién y cuándo recalcula el subtotal del carrito?
}
