package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.SistemaPuntos;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.exceptions.PuntosInsuficientesException;
import com.uade.tpo.wepadel.exceptions.PuntosNegativosException;
import com.uade.tpo.wepadel.exceptions.SistemaPuntosNotFoundException;
import com.uade.tpo.wepadel.repository.SistemaPuntosRepository;

@Service
public class SistemaPuntosServiceImpl implements SistemaPuntosService {

    // 1 punto = $5

    @Autowired
    private SistemaPuntosRepository sistemaPuntosRepository;

    public SistemaPuntos createSistemaPuntos(Usuario usuario) {
        return sistemaPuntosRepository.save(new SistemaPuntos(usuario, 5));
    }

    public Optional<SistemaPuntos> getPuntosByUsuarioId(Long usuarioId) {
        return sistemaPuntosRepository.findByUsuarioId(usuarioId);
    }

    public int calcularPuntosGenerados(BigDecimal montoPagadoEnPesos) {
        if (montoPagadoEnPesos == null || montoPagadoEnPesos.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        return montoPagadoEnPesos.divide(BigDecimal.valueOf(100)).intValue();
    }

    public BigDecimal calcularDescuentoPorPuntos(int puntosUsados, Long usuarioId) {
        SistemaPuntos sistema = sistemaPuntosRepository.findByUsuarioId(usuarioId)
                .orElseThrow(SistemaPuntosNotFoundException::new);

        if (puntosUsados < 0)
            throw new PuntosNegativosException();
        if (puntosUsados > sistema.getCantidad())
            throw new PuntosInsuficientesException();

        return BigDecimal.valueOf((long) puntosUsados * sistema.getConversion());
    }

    public Optional<SistemaPuntos> sumarPuntos(Long usuarioId, int puntosASumar) {
        if (puntosASumar < 0)
            throw new PuntosNegativosException();
        return sistemaPuntosRepository.findByUsuarioId(usuarioId).map(sistema -> {
            sistema.setCantidad(sistema.getCantidad() + puntosASumar);
            return sistemaPuntosRepository.save(sistema);
        });
    }

    public Optional<SistemaPuntos> restarPuntos(Long usuarioId, int puntosARestar) {
        if (puntosARestar < 0)
            throw new PuntosNegativosException();
        return sistemaPuntosRepository.findByUsuarioId(usuarioId).map(sistema -> {
            if (puntosARestar > sistema.getCantidad())
                throw new PuntosInsuficientesException();
            sistema.setCantidad(sistema.getCantidad() - puntosARestar);
            return sistemaPuntosRepository.save(sistema);
        });
    }

}
