package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Compra;
import com.uade.tpo.marketplace.entity.dto.CompraRequest;

public interface CompraService {
    public Optional<Compra> getCompraById(Long compraId);
    public List<Compra> getComprasByUsuarioId(Long usuarioId);
    public Compra createCompra(CompraRequest request);
}
