package com.uade.tpo.wepadel.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.DescuentoRequest;
import com.uade.tpo.wepadel.exceptions.DescuentoInvalidoException;
import com.uade.tpo.wepadel.exceptions.DescuentoNotFoundException;
import com.uade.tpo.wepadel.exceptions.DescuentoSuperpuestoException;
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

        validarSuperposicion(request.getProductoId(), request.getFechaInicio(), request.getFechaFin(), null);

        Descuento descuento = new Descuento(
                producto,
                request.getPorcentaje(),
                request.getFechaInicio(),
                request.getFechaFin()
        );

        if (request.getActivo() != null) {
            descuento.setActivo(request.getActivo());
        }

        return descuentoRepository.save(descuento);
    }

    @Override
    public Optional<Descuento> updateDescuento(Long id, DescuentoRequest request) {
        Descuento descuento = descuentoRepository.findById(id)
                .orElseThrow(DescuentoNotFoundException::new);

        if (request.getPorcentaje() != null) {
            descuento.setPorcentaje(request.getPorcentaje());
        }

        LocalDateTime nuevoInicio = request.getFechaInicio() != null ? request.getFechaInicio() : descuento.getFechaInicio();
        LocalDateTime nuevoFin = request.getFechaFin() != null ? request.getFechaFin() : descuento.getFechaFin();
        boolean nuevoActivo = request.getActivo() != null ? request.getActivo() : descuento.getActivo();

        if (nuevoActivo) {
            validarSuperposicion(descuento.getProducto().getId(), nuevoInicio, nuevoFin, descuento.getId());
        }

        if (request.getFechaInicio() != null) descuento.setFechaInicio(request.getFechaInicio());
        if (request.getFechaFin() != null) descuento.setFechaFin(request.getFechaFin());
        if (request.getActivo() != null) descuento.setActivo(request.getActivo());

        return Optional.of(descuentoRepository.save(descuento));
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

    private void validarSuperposicion(Long productoId, LocalDateTime inicio, LocalDateTime fin, Long descuentoIdIgnorar) {
        if (inicio.isAfter(fin)) throw new DescuentoInvalidoException();
        boolean haySuperposicion = descuentoRepository.findByProductoId(productoId).stream()
                .filter(Descuento::getActivo)
                .filter(d -> descuentoIdIgnorar == null || !d.getId().equals(descuentoIdIgnorar))
                .anyMatch(d -> !(fin.isBefore(d.getFechaInicio()) || inicio.isAfter(d.getFechaFin())));
        if (haySuperposicion) {
            throw new DescuentoSuperpuestoException();
        }
    }
}