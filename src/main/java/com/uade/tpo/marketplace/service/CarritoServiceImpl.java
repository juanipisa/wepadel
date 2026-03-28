package com.uade.tpo.marketplace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Carrito;
import com.uade.tpo.marketplace.entity.CarritoItem;
import com.uade.tpo.marketplace.repository.CarritoItemRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    public Optional<Carrito> getCarritoById(Long carritoId) {
        return carritoRepository.findById(carritoId).map(carrito -> {
            if (carrito.getCreadoEn().isBefore(LocalDateTime.now().minusWeeks(1))) {
                resetCarrito(carritoId);
                carrito.setCreadoEn(LocalDateTime.now());
                carritoRepository.save(carrito);
            }
            return carrito;
        });
    }

    public Carrito createCarrito(Long usuarioId) {
        return carritoRepository.save(new Carrito(usuarioId));
    }

    public List<CarritoItem> getItems(Long carritoId) {
        return carritoItemRepository.findByCarritoId(carritoId);
    }

    public CarritoItem addItem(Long carritoId, Long productoId, int cantidad) {
        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carritoId);
        Optional<CarritoItem> existing = items.stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setCantidad(existing.get().getCantidad() + cantidad);
            return carritoItemRepository.save(existing.get());
        }
        return carritoItemRepository.save(new CarritoItem(carritoId, productoId, cantidad));
    }

    public boolean removeItem(Long carritoId, Long productoId) {
        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carritoId);
        return items.stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst()
                .map(item -> {
                    carritoItemRepository.delete(item);
                    return true;
                }).orElse(false);
    }

    public void resetCarrito(Long carritoId) {
        carritoItemRepository.deleteAll(carritoItemRepository.findByCarritoId(carritoId));
    }

}
