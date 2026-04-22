package com.uade.tpo.wepadel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.UsuarioRequest;
import com.uade.tpo.wepadel.service.UsuarioService;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(usuarioId));
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long usuarioId, @Valid @RequestBody UsuarioRequest usuarioRequest) {
        return ResponseEntity.ok(usuarioService.updateUsuario(usuarioId, usuarioRequest));
    }

}
