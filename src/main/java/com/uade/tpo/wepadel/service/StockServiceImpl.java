package com.uade.tpo.wepadel.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.StockRequest;
import com.uade.tpo.wepadel.exceptions.StockNegativoException;
import com.uade.tpo.wepadel.exceptions.StockNotFoundException;
import com.uade.tpo.wepadel.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock getStockByProductoId(Long productoId) {
        return stockRepository.findByProductoId(productoId)
                .orElseThrow(StockNotFoundException::new);
    }

    public Stock updateStock(Long productoId, StockRequest request) {
        if (request.getCantidad() < 0) {
            throw new StockNegativoException();
        }

        Stock stock = stockRepository.findByProductoId(productoId)
                .orElseThrow(StockNotFoundException::new);

        stock.setCantidad(request.getCantidad());
        stock.setUltimaModificacion(LocalDateTime.now());

        return stockRepository.save(stock);
    }

}