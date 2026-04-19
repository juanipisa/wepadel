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

import jakarta.validation.Valid;

import com.uade.tpo.wepadel.entity.Orden;
import com.uade.tpo.wepadel.entity.dto.OrdenRequest;
import com.uade.tpo.wepadel.exceptions.AccesoDenegadoException;
import com.uade.tpo.wepadel.exceptions.OrdenNotFoundException;
import com.uade.tpo.wepadel.service.OrdenService;

@RestController
@RequestMapping("usuarios/{usuarioId}/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    // Obtener una orden por ID
    @GetMapping("/{ordenId}")
    public ResponseEntity<Orden> getOrdenById(@PathVariable Long usuarioId, @PathVariable Long ordenId) {
        Orden orden = ordenService.getOrdenById(ordenId).orElseThrow(OrdenNotFoundException::new);
        if (!orden.getUsuario().getId().equals(usuarioId)) {
            throw new AccesoDenegadoException();
        }
        return ResponseEntity.ok(orden);
    }

    // Obtener historial de órdenes del usuario
    @GetMapping
    public ResponseEntity<List<Orden>> getOrdenesByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.getOrdenesByUsuarioId(usuarioId));
    }

    // Crear orden (cuando el usuario hace clic en pagar)
    @PostMapping
    public ResponseEntity<Object> createOrden(@PathVariable Long usuarioId,
            @Valid @RequestBody OrdenRequest ordenRequest) {
        if (!ordenRequest.getUsuario().equals(usuarioId)) {
            throw new AccesoDenegadoException();
        }
        Orden result = ordenService.createOrden(ordenRequest);
        return ResponseEntity.created(URI.create("/usuarios/" + usuarioId + "/ordenes/" + result.getId())).body(result);
    }

    // Cancelar orden (dentro de 24h)
    @PutMapping("/{ordenId}/cancelar")
    public ResponseEntity<Orden> cancelarOrden(@PathVariable Long usuarioId, @PathVariable Long ordenId) {
        Orden antes = ordenService.getOrdenById(ordenId).orElseThrow(OrdenNotFoundException::new);
        if (!antes.getUsuario().getId().equals(usuarioId)) {
            throw new AccesoDenegadoException();
        }
        return ResponseEntity.ok(ordenService.cancelarOrden(ordenId).orElseThrow());
    }

}

