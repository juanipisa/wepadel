package com.uade.tpo.wepadel.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.dto.AddImagenRequest;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;
import com.uade.tpo.wepadel.service.ImagenService;

@RestController
@RequestMapping("imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @GetMapping("/{imagenId}")
    public ResponseEntity<ImagenResponse> getImagenById(@PathVariable Long imagenId) {
        return ResponseEntity.ok(imagenService.getImagenById(imagenId));
    }

    @PostMapping
    public ResponseEntity<Void> createImagen(@ModelAttribute AddImagenRequest request) {
        Long id = imagenService.createImagen(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/imagenes/" + id))
                .build();
    }

}
