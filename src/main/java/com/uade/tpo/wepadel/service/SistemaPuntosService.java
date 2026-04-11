package com.uade.tpo.wepadel.service;

import java.util.Optional;

import com.uade.tpo.wepadel.entity.SistemaPuntos;

public interface SistemaPuntosService {
    Optional<SistemaPuntos> getPuntosByUsuarioId(Long usuarioId);
}
