package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.wepadel.entity.Carrito;
import com.uade.tpo.wepadel.entity.CarritoItem;
import com.uade.tpo.wepadel.entity.EstadoOrdenEnum;
import com.uade.tpo.wepadel.entity.Orden;
import com.uade.tpo.wepadel.entity.OrdenItem;
import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.OrdenRequest;
import com.uade.tpo.wepadel.entity.dto.StockRequest;
import com.uade.tpo.wepadel.exceptions.CarritoNotFoundException;
import com.uade.tpo.wepadel.exceptions.CarritoVacioException;
import com.uade.tpo.wepadel.exceptions.OrdenCantBeCancelledException;
import com.uade.tpo.wepadel.exceptions.OrdenNotFoundException;
import com.uade.tpo.wepadel.exceptions.StockInsuficienteException;
import com.uade.tpo.wepadel.exceptions.StockNotFoundException;
import com.uade.tpo.wepadel.exceptions.UsuarioNotFoundException;
import com.uade.tpo.wepadel.repository.CarritoRepository;
import com.uade.tpo.wepadel.repository.OrdenRepository;
import com.uade.tpo.wepadel.repository.StockRepository;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    public Optional<Orden> getOrdenById(Long ordenId) {
        return ordenRepository.findById(ordenId);
    }

    public List<Orden> getOrdenesByUsuarioId(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public Orden createOrden(OrdenRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuario())
                .orElseThrow(UsuarioNotFoundException::new);

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(CarritoNotFoundException::new);

        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }

        for (CarritoItem item : carrito.getItems()) {
            Stock stock = stockRepository.findByProductoId(item.getProducto().getId())
                    .orElseThrow(StockNotFoundException::new);
            if (stock.getCantidad() < item.getCantidad()) {
                throw new StockInsuficienteException();
            }
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CarritoItem item : carrito.getItems()) {
            BigDecimal precio = item.getProducto().getPrecio();
            subtotal = subtotal.add(precio.multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        BigDecimal total = request.getMontoEnvio().add(subtotal);

        Orden orden = new Orden(usuario, request.getDireccion(), request.getCp(),
                request.getMontoEnvio(), subtotal, total, request.getUsaPuntos(), 0);

        for (CarritoItem item : carrito.getItems()) {
            BigDecimal precioSnapshot = item.getProducto().getPrecio();
            orden.getItems().add(new OrdenItem(orden, item.getProducto(), item.getCantidad(), precioSnapshot));
        }

        Orden guardada = ordenRepository.save(orden);

        for (OrdenItem item : guardada.getItems()) {
            Stock stock = stockService.getStockByProductoId(item.getProducto().getId());
            int nuevaCantidad = stock.getCantidad() - item.getCantidad();
            StockRequest stockRequest = new StockRequest();
            stockRequest.setCantidad(nuevaCantidad);
            stockService.updateStock(item.getProducto().getId(), stockRequest);
        }

        carritoService.vaciarCarrito(usuario.getId());

        // TODO: descuento por puntos (cuando esté mergeado)
        // TODO: puntos_generados según regla de negocio
        // TODO: acreditar puntos en SistemaPuntos si corresponde

        return guardada;
    }

    public Optional<Orden> cancelarOrden(Long ordenId) {
        // TODO: Restar puntos_generados del usuario si es CLIENTE registrado
        // TODO: Si usuario consumió puntos, devolverlos
        Orden orden = ordenRepository.findById(ordenId).orElseThrow(OrdenNotFoundException::new);
        if (orden.getEstado() != EstadoOrdenEnum.CONFIRMADA) {
            throw new OrdenCantBeCancelledException();
        }
        if (orden.getFechaCompra().isAfter(LocalDateTime.now().minusHours(24))) {
            throw new OrdenCantBeCancelledException();
        }
        orden.setEstado(EstadoOrdenEnum.CANCELADA);
        ordenRepository.save(orden);
        for (OrdenItem item : orden.getItems()) {
            Stock stock = stockService.getStockByProductoId(item.getProducto().getId());
            int nuevaCantidad = stock.getCantidad() + item.getCantidad();
            StockRequest stockRequest = new StockRequest();
            stockRequest.setCantidad(nuevaCantidad);
            stockService.updateStock(item.getProducto().getId(), stockRequest);
        }
        return Optional.of(orden);
    }

}
