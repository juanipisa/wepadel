package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Orden;
import com.uade.tpo.marketplace.entity.dto.OrdenRequest;

public interface CompraService {
    public Optional<Orden> getCompraById(Long compraId);
    public List<Orden> getComprasByUsuarioId(Long usuarioId);
    public Orden createCompra(OrdenRequest request);
}
