package com.uade.tpo.wepadel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.Orden;
import com.uade.tpo.wepadel.service.OrdenService;

@RestController
@RequestMapping("ordenes")
public class OrdenAdminController {

    @Autowired
    private OrdenService ordenService;

    // Obtener todas las ordenes (solo ADMIN)
    @GetMapping
    public ResponseEntity<List<Orden>> getAllOrdenes() {
        return ResponseEntity.ok(ordenService.getAllOrdenes());
    }
}
