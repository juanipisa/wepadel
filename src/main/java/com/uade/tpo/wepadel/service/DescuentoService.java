package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.dto.DescuentoRequest;

public interface DescuentoService {

    Descuento createDescuento(DescuentoRequest request);

    Optional<Descuento> updateDescuento(Long id, DescuentoRequest request);

    List<Descuento> getDescuentosByProductoId(Long productoId);

    Descuento getDescuentoById(Long id);

    Optional<Descuento> getDescuentoVigente(Long productoId);

    void deleteDescuento(Long id);
}