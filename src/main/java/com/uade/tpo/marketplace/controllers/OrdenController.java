package com.uade.tpo.marketplace.controllers;

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

import com.uade.tpo.marketplace.entity.Orden;
import com.uade.tpo.marketplace.entity.dto.OrdenRequest;
import com.uade.tpo.marketplace.service.OrdenService;

@RestController
@RequestMapping("usuarios/{usuarioId}/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    // Obtener una orden por ID
    @GetMapping("/{ordenId}")
    public ResponseEntity<Orden> getOrdenById(@PathVariable Long usuarioId, @PathVariable Long ordenId) {
        Optional<Orden> orden = ordenService.getOrdenById(ordenId);
        if (orden.isPresent()) {
            return ResponseEntity.ok(orden.get());
        }
        return ResponseEntity.noContent().build();
    }

    // Obtener historial de órdenes del usuario
    @GetMapping
    public ResponseEntity<List<Orden>> getOrdenesByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.getOrdenesByUsuarioId(usuarioId));
    }

    // Crear orden (cuando el usuario hace clic en pagar)
    @PostMapping
    public ResponseEntity<Object> createOrden(@PathVariable Long usuarioId, @RequestBody OrdenRequest ordenRequest) {
        //TODO: Validar que usuarioId coincide con el del request
        //TODO: Validar que usuario es CLIENTE (no ADMIN)
        Orden result = ordenService.createOrden(ordenRequest);
        return ResponseEntity.created(URI.create("/usuarios/" + usuarioId + "/ordenes/" + result.getId())).body(result);
    }

    // Cancelar orden (dentro de 24h)
    @PutMapping("/{ordenId}/cancelar")
    public ResponseEntity<Orden> cancelarOrden(@PathVariable Long usuarioId, @PathVariable Long ordenId) {
        //TODO: Validar que usuarioId es el dueño de la orden
        Optional<Orden> orden = ordenService.cancelarOrden(ordenId);
        if (orden.isPresent()) {
            return ResponseEntity.ok(orden.get());
        }
        return ResponseEntity.noContent().build();
    }

}

