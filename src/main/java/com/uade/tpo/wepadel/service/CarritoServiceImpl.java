package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Carrito;
import com.uade.tpo.wepadel.entity.CarritoItem;
import com.uade.tpo.wepadel.entity.dto.CarritoItemRequest;
import com.uade.tpo.wepadel.repository.CarritoItemRepository;
import com.uade.tpo.wepadel.repository.CarritoRepository;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    public Optional<Carrito> getCarritoByUsuarioId(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public Carrito createCarrito(Long usuarioId) {
        return carritoRepository.save(new Carrito(usuarioId));
    }

    public List<CarritoItem> getItems(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isPresent()) {
            return carritoItemRepository.findByCarritoId(carrito.get().getId());
        }
        return List.of();
    }

    public CarritoItem addItem(Long usuarioId, CarritoItemRequest request) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isEmpty()) {
            throw new RuntimeException("Carrito no encontrado para usuario: " + usuarioId);
        }

        Long carritoId = carrito.get().getId();
        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carritoId);
        
        // Si el producto ya existe en el carrito, incrementar cantidad
        Optional<CarritoItem> existing = items.stream()
                .filter(i -> i.getProductoId().equals(request.getProductoId()))
                .findFirst();
        
        if (existing.isPresent()) {
            existing.get().setCantidad(existing.get().getCantidad() + request.getCantidad());
            return carritoItemRepository.save(existing.get());
        }
        
        return carritoItemRepository.save(new CarritoItem(carritoId, request.getProductoId(), request.getCantidad()));
    }

    public boolean removeItem(Long usuarioId, Long productoId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isEmpty()) {
            return false;
        }

        List<CarritoItem> items = carritoItemRepository.findByCarritoId(carrito.get().getId());
        return items.stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst()
                .map(item -> {
                    carritoItemRepository.delete(item);
                    return true;
                }).orElse(false);
    }

    public void deleteCarrito(Long usuarioId) {
        Optional<Carrito> carrito = carritoRepository.findByUsuarioId(usuarioId);
        if (carrito.isPresent()) {
            // Eliminar todos los items del carrito
            List<CarritoItem> items = carritoItemRepository.findByCarritoId(carrito.get().getId());
            carritoItemRepository.deleteAll(items);
            // Eliminar el carrito
            carritoRepository.delete(carrito.get());
        }
    }

}