package com.uade.tpo.marketplace.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Carrito;
import com.uade.tpo.marketplace.entity.CarritoItem;
import com.uade.tpo.marketplace.entity.dto.CarritoItemRequest;
import com.uade.tpo.marketplace.service.CarritoService;

@RestController
@RequestMapping("carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{carritoId}")
    public ResponseEntity<Carrito> getCarritoById(@PathVariable Long carritoId) {
        Optional<Carrito> carrito = carritoService.getCarritoById(carritoId);
        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{carritoId}/items")
    public ResponseEntity<List<CarritoItem>> getItems(@PathVariable Long carritoId) {
        return ResponseEntity.ok(carritoService.getItems(carritoId));
    }

    @PostMapping("/{carritoId}/items")
    public ResponseEntity<CarritoItem> addItem(@PathVariable Long carritoId, @RequestBody CarritoItemRequest request) {
        CarritoItem item = carritoService.addItem(carritoId, request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{carritoId}/items/{productoId}")
    public ResponseEntity<Object> removeItem(@PathVariable Long carritoId, @PathVariable Long productoId) {
        boolean removed = carritoService.removeItem(carritoId, productoId);
        if (removed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

}
