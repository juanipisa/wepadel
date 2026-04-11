package com.uade.tpo.wepadel.service;

import java.util.Optional;

import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.StockRequest;

public interface StockService {
    public Optional<Stock> getStockByProductoId(Long productoId);
    public Stock createStock(Long productoId, int cantidad);
    public Optional<Stock> updateStock(Long productoId, StockRequest request);
    //TODO: Se actualiza stock cuando se crea orden o solo cuando se cancela?
}
