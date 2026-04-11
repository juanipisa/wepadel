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
    //TODO: Cuál es la fórmula exacta para calcular puntos_generados?
    //TODO: Quién crea los OrdenItem? En qué momento?
    //TODO: Qué validaciones se hacen antes de confirmar la orden?
    //TODO: Se actualiza stock al crear orden?
    //TODO: Se suma puntos al usuario al confirmar orden?
    //TODO: Cuáles son las excepciones que puede lanzar?
}
