package com.uade.tpo.wepadel.service;

import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.StockRequest;

public interface StockService {
    Stock getStockByProductoId(Long productoId);
    Stock updateStock(Long productoId, StockRequest request);
}