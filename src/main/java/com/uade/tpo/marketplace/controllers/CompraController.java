package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Compra;
import com.uade.tpo.marketplace.entity.dto.CompraRequest;
import com.uade.tpo.marketplace.service.CompraService;

@RestController
@RequestMapping("compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @GetMapping("/{compraId}")
    public ResponseEntity<Compra> getCompraById(@PathVariable Long compraId) {
        Optional<Compra> compra = compraService.getCompraById(compraId);
        if (compra.isPresent()) {
            return ResponseEntity.ok(compra.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Compra>> getComprasByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(compraService.getComprasByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Object> createCompra(@RequestBody CompraRequest compraRequest) {
        Compra result = compraService.createCompra(compraRequest);
        return ResponseEntity.created(URI.create("/compras/" + result.getId())).body(result);
    }

}
