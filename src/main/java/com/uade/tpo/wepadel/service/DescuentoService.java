package com.uade.tpo.wepadel.service;

import java.util.List;

import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.dto.DescuentoRequest;

public interface DescuentoService {

    Descuento createDescuento(DescuentoRequest request);

    List<Descuento> getDescuentosByProductoId(Long productoId);

    Descuento getDescuentoById(Long id);

    void deleteDescuento(Long id);
}