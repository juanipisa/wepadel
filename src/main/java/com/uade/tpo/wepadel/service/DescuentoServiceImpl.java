package com.uade.tpo.wepadel.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.DescuentoRequest;
import com.uade.tpo.wepadel.exceptions.DescuentoNotFoundException;
import com.uade.tpo.wepadel.exceptions.ProductoNotFoundException;
import com.uade.tpo.wepadel.repository.DescuentoRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;

@Service
public class DescuentoServiceImpl implements DescuentoService {

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Descuento createDescuento(DescuentoRequest request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(ProductoNotFoundException::new);

        Descuento descuento = new Descuento(
                producto,
                request.getPorcentaje(),
                request.getFechaInicio(),
                request.getFechaFin()
        );

        return descuentoRepository.save(descuento);
    }

    @Override
    public List<Descuento> getDescuentosByProductoId(Long productoId) {
        return descuentoRepository.findByProductoId(productoId);
    }

    @Override
    public Descuento getDescuentoById(Long id) {
        return descuentoRepository.findById(id)
                .orElseThrow(DescuentoNotFoundException::new);
    }

    @Override
    public void deleteDescuento(Long id) {
        descuentoRepository.deleteById(id);
    }

    @Override
    public Optional<Descuento> getDescuentoVigente(Long productoId) {
        LocalDateTime ahora = LocalDateTime.now();
        return descuentoRepository.findByProductoId(productoId)
                .stream()
                .filter(d -> d.getActivo()
                        && !ahora.isBefore(d.getFechaInicio())
                        && !ahora.isAfter(d.getFechaFin()))
                .findFirst();
    }
}