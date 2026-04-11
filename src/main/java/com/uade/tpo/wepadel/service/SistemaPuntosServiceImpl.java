package com.uade.tpo.wepadel.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.SistemaPuntos;
import com.uade.tpo.wepadel.repository.SistemaPuntosRepository;

@Service
public class SistemaPuntosServiceImpl implements SistemaPuntosService {

    @Autowired
    private SistemaPuntosRepository sistemaPuntosRepository;

    public Optional<SistemaPuntos> getPuntosByUsuarioId(Long usuarioId) {
        return sistemaPuntosRepository.findByUsuarioId(usuarioId);
    }

}
