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
@RequestMapping("usuarios/{usuarioId}/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<Carrito> getCarritoByUsuarioId(@PathVariable Long usuarioId) {
        Optional<Carrito> carrito = carritoService.getCarritoByUsuarioId(usuarioId);
        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito.get());
        }
        return ResponseEntity.noContent().build();
    }

    // Obtener items del carrito del usuario
    @GetMapping("/items")
    public ResponseEntity<List<CarritoItem>> getItems(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.getItems(usuarioId));
    }

    // Agregar item al carrito del usuario
    @PostMapping("/items")
    public ResponseEntity<CarritoItem> addItem(@PathVariable Long usuarioId, @RequestBody CarritoItemRequest request) {
        CarritoItem item = carritoService.addItem(usuarioId, request);
        return ResponseEntity.ok(item);
    }

    // Eliminar producto del carrito del usuario
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<Object> removeItem(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        boolean removed = carritoService.removeItem(usuarioId, productoId);
        if (removed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    // Borrar carrito completo del usuario
    @DeleteMapping
    public ResponseEntity<Object> deleteCarrito(@PathVariable Long usuarioId) {
        carritoService.deleteCarrito(usuarioId);
        return ResponseEntity.ok().build();
    }

}
