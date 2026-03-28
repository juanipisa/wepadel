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

import com.uade.tpo.marketplace.entity.Categoria;
import com.uade.tpo.marketplace.entity.dto.CategoriaRequest;
import com.uade.tpo.marketplace.exceptions.CategoriaDuplicateException;
import com.uade.tpo.marketplace.service.CategoriaService;

@RestController
@RequestMapping("categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> getCategorias() {
        return categoriaService.getCategorias();
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long categoriaId) {
        Optional<Categoria> categoria = categoriaService.getCategoriaById(categoriaId);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createCategoria(@RequestBody CategoriaRequest categoriaRequest) throws CategoriaDuplicateException {
        Categoria result = categoriaService.createCategoria(categoriaRequest.getDescripcion());
        return ResponseEntity.created(URI.create("/categorias/" + result.getId())).body(result);
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaRequest categoriaRequest) {
        Optional<Categoria> categoria = categoriaService.updateCategoria(categoriaId, categoriaRequest.getDescripcion(), categoriaRequest.getHabilitada());
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        }
        return ResponseEntity.noContent().build();
    }

}
