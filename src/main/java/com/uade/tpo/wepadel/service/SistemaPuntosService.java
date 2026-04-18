package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.SistemaPuntos;

public interface SistemaPuntosService {
    public Optional<SistemaPuntos> getPuntosByUsuarioId(Long usuarioId);
    //busca registro puntos usuario, lo devuelve o vacio

    public int calcularPuntosGenerados(BigDecimal montoPagadoEnPesos);
    //calcula cuantos puntos genera una compra. no negativos. no usar más puntos de los que tiene

    public BigDecimal calcularDescuentoPorPuntos(int puntosUsados, Long usuarioId);
    //convierte puntos en $

    public Optional<SistemaPuntos> sumarPuntos(Long usuarioId, int puntosASumar);
    //suma puntos al usuario

    public Optional<SistemaPuntos> restarPuntos(Long usuarioId, int puntosARestar);
    //le saca puntos al usuario cuando los usa
}


