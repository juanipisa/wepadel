package com.uade.tpo.wepadel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.StockRequest;
import com.uade.tpo.wepadel.service.StockService;

@RestController
@RequestMapping("stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Stock> getStockByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(stockService.getStockByProductoId(productoId));
    }

    @PutMapping("/producto/{productoId}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long productoId, @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.updateStock(productoId, request));
    }

}