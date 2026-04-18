package com.uade.tpo.wepadel.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;
import com.uade.tpo.wepadel.service.ImagenService;
import com.uade.tpo.wepadel.service.ProductoService;

@RestController
@RequestMapping("productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ImagenService imagenService;

    @GetMapping
    public ResponseEntity<List<Producto>> getProductos() {
        return ResponseEntity.ok(productoService.getProductos());
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long productoId) {
        return ResponseEntity.ok(productoService.getProductoById(productoId));
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoRequest request) {
        Producto result = productoService.createProducto(request);
        return ResponseEntity.created(URI.create("/productos/" + result.getId())).body(result);
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long productoId, @RequestBody ProductoRequest request) {
        return productoService.updateProducto(productoId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{productoId}/imagenes")
    public ResponseEntity<List<ImagenResponse>> getImagenesByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(imagenService.getImagenesByProductoId(productoId));
    }

}
