package com.uade.tpo.wepadel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.Carrito;
import com.uade.tpo.wepadel.entity.CarritoItem;
import com.uade.tpo.wepadel.entity.dto.CarritoItemRequest;
import com.uade.tpo.wepadel.service.CarritoService;

@RestController
@RequestMapping("usuarios/{usuarioId}/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<Carrito> getCarritoByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.getCarritoByUsuarioId(usuarioId));
    }

    @GetMapping("/items")
    public ResponseEntity<List<CarritoItem>> getItems(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.getItems(usuarioId));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoItem> addItem(@PathVariable Long usuarioId, @RequestBody CarritoItemRequest request) {
        return ResponseEntity.ok(carritoService.addItem(usuarioId, request));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        carritoService.removeItem(usuarioId, productoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.ok().build();
    }
}
