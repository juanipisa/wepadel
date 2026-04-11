package com.uade.tpo.wepadel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Orden;
import com.uade.tpo.wepadel.entity.EstadoOrdenEnum;
import com.uade.tpo.wepadel.entity.dto.OrdenRequest;
import com.uade.tpo.wepadel.repository.OrdenRepository;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    public Optional<Orden> getOrdenById(Long ordenId) {
        return ordenRepository.findById(ordenId);
    }

    public List<Orden> getOrdenesByUsuarioId(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }

    public Orden createOrden(OrdenRequest request) {
        //TODO: Validar que usuario existe
        //TODO: Validar que usuario es CLIENTE (no ADMIN)
        //TODO: Validar que carrito existe y tiene items
        //TODO: Validar que todos los productos tienen stock
        //TODO: Obtener carrito del usuario y crear OrdenItem para cada CarritoItem
        //TODO: Calcular subtotal desde CarritoItem
        //TODO: Calcular total = montoEnvio + subtotal - (puntos usados)
        //TODO: Calcular puntos_generados con la fórmula definida
        //TODO: Crear OrdenItem para cada item del carrito
        //TODO: Actualizar stock de los productos
        //TODO: Sumar puntos a SistemaPuntos si usuario está registrado y es CLIENTE
        //TODO: Borrar carrito
        
        Orden orden = new Orden(request.getUsuarioId(), request.getDireccion(), request.getCp(),
                request.getMontoEnvio(), null, null, request.getUsaPuntos(), 0);
        
        return ordenRepository.save(orden);
    }

    public Optional<Orden> cancelarOrden(Long ordenId) {
        //TODO: Validar que orden existe
        //TODO: Validar que han pasado menos de 24h desde la creación
        //TODO: Validar que estado es CONFIRMADA
        //TODO: Cambiar estado a CANCELADA
        //TODO: Devolver stock de los productos
        //TODO: Restar puntos_generados del usuario si es CLIENTE registrado
        //TODO: Si usuario consumió puntos, devolverlos
        //TODO: Manejar excepciones (OrderNoEncontrada, OrdenNoPuedeCancelarse, etc)
        
        return ordenRepository.findById(ordenId).map(orden -> {
            orden.setEstado(EstadoOrdenEnum.CANCELADA);
            return ordenRepository.save(orden);
        });
    }

}
