package com.uade.tpo.wepadel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.dto.DescuentoRequest;
import com.uade.tpo.wepadel.service.DescuentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    @PostMapping
    public ResponseEntity<Descuento> createDescuento(@Valid @RequestBody DescuentoRequest request) {
        Descuento descuento = descuentoService.createDescuento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(descuento);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Descuento>> getDescuentosByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(descuentoService.getDescuentosByProductoId(productoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Descuento> getDescuentoById(@PathVariable Long id) {
        return ResponseEntity.ok(descuentoService.getDescuentoById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDescuento(@PathVariable Long id) {
        descuentoService.deleteDescuento(id);
        return ResponseEntity.noContent().build();
    }
}