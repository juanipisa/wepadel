package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.Orden;
import com.uade.tpo.wepadel.entity.dto.OrdenRequest;

public interface OrdenService {
    public Optional<Orden> getOrdenById(Long ordenId);
    public List<Orden> getOrdenesByUsuarioId(Long usuarioId);
    public Orden createOrden(OrdenRequest request);
    public Optional<Orden> cancelarOrden(Long ordenId);
}
