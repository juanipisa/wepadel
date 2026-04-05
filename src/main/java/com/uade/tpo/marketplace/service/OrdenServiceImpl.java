package com.uade.tpo.marketplace.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Orden;
import com.uade.tpo.marketplace.entity.OrdenItem;
import com.uade.tpo.marketplace.entity.EstadoOrdenEnum;
import com.uade.tpo.marketplace.entity.dto.OrdenRequest;
import com.uade.tpo.marketplace.repository.CarritoItemRepository;
import com.uade.tpo.marketplace.repository.CompraItemRepository;
import com.uade.tpo.marketplace.repository.CompraRepository;
import com.uade.tpo.marketplace.repository.ProductoRepository;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraItemRepository compraItemRepository;

    @Autowired
    private DatosFacturacionRepository datosFacturacionRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Optional<Orden> getCompraById(Long compraId) {
        return compraRepository.findById(compraId);
    }

    public List<Orden> getComprasByUsuarioId(Long usuarioId) {
        return compraRepository.findByUsuarioId(usuarioId);
    }

    public Orden createCompra(OrdenRequest request) {
        List<OrdenItem> items = carritoItemRepository.findByCarritoId(request.getCarritoId())
                .stream()
                .map(carritoItem -> {
                    Double precio = productoRepository.findById(carritoItem.getProductoId())
                            .map(p -> p.getPrecio())
                            .orElse(0.0);
                    return new OrdenItem(null, carritoItem.getProductoId(), carritoItem.getCantidad(), precio);
                }).toList();

        Double montoProductos = items.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();

        Orden compra = new Orden();
        compra.setUsuarioId(request.getUsuarioId());
        compra.setDireccion(request.getDireccion());
        compra.setCp(request.getCp());
        compra.setMontoEnvio(request.getMontoEnvio());
        compra.setMontoFinal(montoProductos + request.getMontoEnvio());
        compra.setEstado(EstadoOrdenEnum.PENDIENTE);
        compra.setFecha(LocalDateTime.now());
        Orden savedCompra = compraRepository.save(compra);

        items.forEach(item -> {
            item.setCompraId(savedCompra.getId());
            compraItemRepository.save(item);
        });

        DatosFacturacion datos = new DatosFacturacion();
        datos.setCompraId(savedCompra.getId());
        datos.setNroTarjeta(request.getNroTarjeta());
        datos.setVencimiento(request.getVencimiento());
        datos.setDni(request.getDni());
        datos.setCvv(request.getCvv());
        datos.setNombreTitular(request.getNombreTitular());
        datos.setCuotas(request.getCuotas());
        datosFacturacionRepository.save(datos);

        return savedCompra;
    }

}
