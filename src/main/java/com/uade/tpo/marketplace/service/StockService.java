package com.uade.tpo.marketplace.service;

import java.util.Optional;

import com.uade.tpo.marketplace.entity.Stock;
import com.uade.tpo.marketplace.entity.dto.StockRequest;

public interface StockService {
    public Optional<Stock> getStockByProductoId(Long productoId);
    public Stock createStock(Long productoId, int cantidad);
    public Optional<Stock> updateStock(Long productoId, StockRequest request);
}
