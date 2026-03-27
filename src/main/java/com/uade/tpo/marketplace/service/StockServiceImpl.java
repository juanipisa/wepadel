package com.uade.tpo.marketplace.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Stock;
import com.uade.tpo.marketplace.entity.dto.StockRequest;
import com.uade.tpo.marketplace.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    public Optional<Stock> getStockByProductoId(Long productoId) {
        return stockRepository.findByProductoId(productoId);
    }

    public Stock createStock(Long productoId, int cantidad) {
        return stockRepository.save(new Stock(productoId, cantidad));
    }

    public Optional<Stock> updateStock(Long productoId, StockRequest request) {
        return stockRepository.findByProductoId(productoId).map(stock -> {
            stock.setCantidad(request.getCantidad());
            stock.setLastUpdated(LocalDateTime.now());
            return stockRepository.save(stock);
        });
    }

}
