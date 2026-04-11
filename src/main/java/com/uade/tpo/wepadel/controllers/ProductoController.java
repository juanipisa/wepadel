package com.uade.tpo.wepadel.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
import com.uade.tpo.wepadel.entity.dto.ProductoRequest;
import com.uade.tpo.wepadel.service.ProductoService;

@RestController
@RequestMapping("productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getProductos() {
        return productoService.getProductos();
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long productoId) {
        Optional<Producto> product = productoService.getProductoById(productoId);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createProducto(@RequestBody ProductoRequest productRequest) {
        //TODO: Validar que el usuario autenticado es ADMIN
        Producto result = productoService.createProducto(productRequest);
        return ResponseEntity.created(URI.create("/productos/" + result.getId())).body(result);
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long productoId, @RequestBody ProductoRequest productRequest) {
        //TODO: Validar que el usuario autenticado es ADMIN
        Optional<Producto> product = productoService.updateProducto(productoId, productRequest);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }
        return ResponseEntity.noContent().build();
    }

}
