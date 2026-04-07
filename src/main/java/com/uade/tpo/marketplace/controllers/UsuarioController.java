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

import com.uade.tpo.marketplace.entity.Usuario;
import com.uade.tpo.marketplace.entity.dto.UsuarioRequest;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.service.UsuarioService;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(usuarioId);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.noContent().build();
    }

    // Crear usuario invitado (sin parámetros en body)
    @PostMapping("/invitado")
    public ResponseEntity<Object> createUsuarioInvitado() throws UsuarioDuplicateException {
        Usuario result = usuarioService.createUsuario(new UsuarioRequest());
        return ResponseEntity.created(URI.create("/usuarios/" + result.getId())).body(result);
    }

    // Crear usuario registrado (con datos completos)
    @PostMapping("/registrado")
    public ResponseEntity<Object> createUsuarioRegistrado(@RequestBody UsuarioRequest usuarioRequest) throws UsuarioDuplicateException {
        Usuario result = usuarioService.createUsuario(usuarioRequest);
        return ResponseEntity.created(URI.create("/usuarios/" + result.getId())).body(result);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long usuarioId, @RequestBody UsuarioRequest usuarioRequest) {
        Optional<Usuario> usuario = usuarioService.updateUsuario(usuarioId, usuarioRequest);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.noContent().build();
    }

}
