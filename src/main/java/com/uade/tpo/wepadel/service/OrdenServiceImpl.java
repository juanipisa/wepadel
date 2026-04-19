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
import com.uade.tpo.wepadel.entity.Descuento;
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
import com.uade.tpo.wepadel.exceptions.PuntosCanjeInvalidoException;
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
    private DescuentoService descuentoService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private SistemaPuntosService sistemaPuntosService;

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
        
        if (Boolean.TRUE.equals(request.getUsaPuntos())
                && (request.getPuntosUsados() == null || request.getPuntosUsados() <= 0)) {
            throw new PuntosCanjeInvalidoException();
        }

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
    
        Orden orden = new Orden(usuario, request.getDireccion(), request.getCp(),
                request.getMontoEnvio(), BigDecimal.ZERO, BigDecimal.ZERO,
                request.getUsaPuntos(), 0, 0);
    
        BigDecimal subtotal = calcularSubtotalConDescuentos(carrito.getItems(), orden);
        BigDecimal total = request.getMontoEnvio().add(subtotal);
    
        int puntosUsadosEnCompra = 0;
        if (Boolean.TRUE.equals(request.getUsaPuntos()) && request.getPuntosUsados() != null
                && request.getPuntosUsados() > 0) {
            puntosUsadosEnCompra = request.getPuntosUsados();
            BigDecimal descuentoPuntos = sistemaPuntosService.calcularDescuentoPorPuntos(puntosUsadosEnCompra, usuario.getId());
            total = total.subtract(descuentoPuntos);
            if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;
            sistemaPuntosService.restarPuntos(usuario.getId(), puntosUsadosEnCompra);
        }
    
        int puntosGenerados = sistemaPuntosService.calcularPuntosGenerados(total);
        if (puntosGenerados > 0) sistemaPuntosService.sumarPuntos(usuario.getId(), puntosGenerados);
    
        orden.setSubtotal(subtotal);
        orden.setTotal(total);
        orden.setPuntosGenerados(puntosGenerados);
        orden.setPuntosUsados(puntosUsadosEnCompra);
    
        Orden guardada = ordenRepository.save(orden);
    
        ajustarStock(guardada.getItems(),-1);
        carritoService.vaciarCarrito(usuario.getId());
    
        return guardada;
    }

    @Transactional
    public Optional<Orden> cancelarOrden(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(OrdenNotFoundException::new);
    
        if (orden.getEstado() != EstadoOrdenEnum.CONFIRMADA) {
            throw new OrdenCantBeCancelledException(
                    "Solo se pueden cancelar órdenes en estado CONFIRMADA");
        }
        if (orden.getFechaCompra().isBefore(LocalDateTime.now().minusHours(24))) {
            throw new OrdenCantBeCancelledException(
                    "Solo se puede cancelar dentro de las 24 horas posteriores a la compra");
        }
    
        orden.setEstado(EstadoOrdenEnum.CANCELADA);
        ordenRepository.save(orden);
    
        ajustarStock(orden.getItems(),1);
    
        Long uid = orden.getUsuario().getId();
        if (orden.getPuntosGenerados() > 0) {
            sistemaPuntosService.restarPuntos(uid, orden.getPuntosGenerados());
        }
        if (orden.getPuntosUsados() > 0) {
            sistemaPuntosService.sumarPuntos(uid, orden.getPuntosUsados());
        }
    
        return Optional.of(orden);
    }

    private BigDecimal calcularSubtotalConDescuentos(List<CarritoItem> items, Orden orden) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CarritoItem item : items) {
            BigDecimal precioOriginal = item.getProducto().getPrecio();
            BigDecimal precioFinal = precioOriginal;
    
            Optional<Descuento> descuento = descuentoService.getDescuentoVigente(item.getProducto().getId());
            if (descuento.isPresent()) {
                BigDecimal factor = BigDecimal.ONE.subtract(
                    descuento.get().getPorcentaje().divide(BigDecimal.valueOf(100)));
                precioFinal = precioOriginal.multiply(factor);
            }
    
            subtotal = subtotal.add(precioFinal.multiply(BigDecimal.valueOf(item.getCantidad())));
            orden.getItems().add(new OrdenItem(orden, item.getProducto(), item.getCantidad(), precioFinal));
        }
        return subtotal;
    }
    
    private void ajustarStock(List<OrdenItem> items, int signo) {
        for (OrdenItem item : items) {
            Stock stock = stockService.getStockByProductoId(item.getProducto().getId());
            StockRequest stockRequest = new StockRequest();
            stockRequest.setCantidad(stock.getCantidad() + (signo * item.getCantidad()));
            stockService.updateStock(item.getProducto().getId(), stockRequest);
        }
    }

}
