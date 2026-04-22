package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.uade.tpo.wepadel.entity.SistemaPuntos;
import com.uade.tpo.wepadel.entity.Usuario;

public interface SistemaPuntosService {
    public SistemaPuntos createSistemaPuntos(Usuario usuario);
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

    public Optional<SistemaPuntos> ajustarPuntos(Long usuarioId, int puntosARestar);
}


