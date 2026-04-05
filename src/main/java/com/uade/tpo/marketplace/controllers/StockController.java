package com.uade.tpo.marketplace.controllers;

import java.net.URI;
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

import com.uade.tpo.marketplace.entity.Stock;
import com.uade.tpo.marketplace.entity.dto.StockRequest;
import com.uade.tpo.marketplace.service.StockService;

@RestController
@RequestMapping("stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Stock> getStockByProductoId(@PathVariable Long productoId) {
        Optional<Stock> stock = stockService.getStockByProductoId(productoId);
        if (stock.isPresent()) {
            return ResponseEntity.ok(stock.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createStock(@RequestBody StockRequest stockRequest) {
        //TODO: Validar que el usuario autenticado es ADMIN
        Stock result = stockService.createStock(stockRequest.getProductoId(), stockRequest.getCantidad());
        return ResponseEntity.created(URI.create("/stocks/producto/" + result.getProductoId())).body(result);
    }

    @PutMapping("/producto/{productoId}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long productoId, @RequestBody StockRequest stockRequest) {
        //TODO: Validar que el usuario autenticado es ADMIN
        Optional<Stock> stock = stockService.updateStock(productoId, stockRequest);
        if (stock.isPresent()) {
            return ResponseEntity.ok(stock.get());
        }
        return ResponseEntity.noContent().build();
    }

}
