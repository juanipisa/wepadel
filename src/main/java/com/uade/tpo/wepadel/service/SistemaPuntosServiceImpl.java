package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.SistemaPuntos;
import com.uade.tpo.wepadel.repository.SistemaPuntosRepository;

@Service
public class SistemaPuntosServiceImpl implements SistemaPuntosService {

    private static final int conversion = 5; // 1 punto = $5

    @Autowired
    private SistemaPuntosRepository sistemaPuntosRepository;

    public Optional<SistemaPuntos> getPuntosByUsuarioId(Long usuarioId) {
        return sistemaPuntosRepository.findByUsuarioId(usuarioId);
    }

    public SistemaPuntos inicializarSistemaPuntos(Long usuarioId) {
        return sistemaPuntosRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> sistemaPuntosRepository.save(new SistemaPuntos(usuarioId, conversion)));
    }

    public int calcularPuntosGenerados(BigDecimal montoPagadoEnPesos) {
        if (montoPagadoEnPesos == null || montoPagadoEnPesos.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        return montoPagadoEnPesos.divide(BigDecimal.valueOf(100)).intValue();
    }

    public BigDecimal calcularDescuento(int puntosUsados, Long usuarioId) {
        SistemaPuntos sistema = sistemaPuntosRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene sistema de puntos inicializado"));

        if (puntosUsados < 0) {
            throw new RuntimeException("La cantidad de puntos a usar no puede ser negativa");
        }

        if (puntosUsados > sistema.getCantidad()) {
            throw new RuntimeException("El usuario no tiene suficientes puntos");
        }

        return BigDecimal.valueOf((long) puntosUsados * sistema.getConversion());
    }

    public Optional<SistemaPuntos> sumarPuntos(Long usuarioId, int puntosASumar) {
        if (puntosASumar < 0) {
            throw new RuntimeException("No se pueden sumar puntos negativos");
        }

        return sistemaPuntosRepository.findByUsuarioId(usuarioId).map(sistema -> {
            sistema.setCantidad(sistema.getCantidad() + puntosASumar);
            return sistemaPuntosRepository.save(sistema);
        });
    }

    public Optional<SistemaPuntos> restarPuntos(Long usuarioId, int puntosARestar) {
        if (puntosARestar < 0) {
            throw new RuntimeException("No se pueden restar puntos negativos");
        }

        return sistemaPuntosRepository.findByUsuarioId(usuarioId).map(sistema -> {
            if (puntosARestar > sistema.getCantidad()) {
                throw new RuntimeException("No hay suficientes puntos para canjear");
            }

            sistema.setCantidad(sistema.getCantidad() - puntosARestar);
            return sistemaPuntosRepository.save(sistema);
        });
    }


























    
}
