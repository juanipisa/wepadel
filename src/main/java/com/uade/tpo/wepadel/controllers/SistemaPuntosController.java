package com.uade.tpo.wepadel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.SistemaPuntos;
import com.uade.tpo.wepadel.service.SistemaPuntosService;

@RestController
@RequestMapping("usuarios/{usuarioId}/puntos")
public class SistemaPuntosController {

    @Autowired
    private SistemaPuntosService sistemaPuntosService;

    @GetMapping
    public ResponseEntity<SistemaPuntos> getPuntosByUsuarioId(@PathVariable Long usuarioId) {
        return sistemaPuntosService.getPuntosByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

}
