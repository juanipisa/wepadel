package com.uade.tpo.wepadel.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.Carrito;
import com.uade.tpo.wepadel.entity.CarritoItem;
import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.RolEnum;
import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.Usuario;
import com.uade.tpo.wepadel.entity.dto.CarritoItemRequest;
import com.uade.tpo.wepadel.exceptions.AccesoDenegadoException;
import com.uade.tpo.wepadel.exceptions.CantidadInvalidaException;
import com.uade.tpo.wepadel.exceptions.CarritoItemNotFoundException;
import com.uade.tpo.wepadel.exceptions.CarritoNotFoundException;
import com.uade.tpo.wepadel.exceptions.ProductoNoHabilitadoException;
import com.uade.tpo.wepadel.exceptions.ProductoNotFoundException;
import com.uade.tpo.wepadel.exceptions.StockInsuficienteException;
import com.uade.tpo.wepadel.exceptions.UsuarioNotFoundException;
import com.uade.tpo.wepadel.repository.CarritoItemRepository;
import com.uade.tpo.wepadel.repository.CarritoRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;
import com.uade.tpo.wepadel.repository.StockRepository;
import com.uade.tpo.wepadel.repository.UsuarioRepository;

@Service
public class CarritoServiceImpl implements CarritoService {

    private static final int DIAS_EXPIRACION = 7;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DescuentoServiceImpl descuentoServiceImpl;

    public Carrito getCarritoByUsuarioId(Long usuarioId) {
        Carrito carrito = validarYObtenerCarrito(usuarioId);
        recalcularSubtotal(carrito);
        return carrito;
    }

    public Carrito createCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNotFoundException::new);
        return carritoRepository.save(new Carrito(usuario));
    }

    public List<CarritoItem> getItems(Long usuarioId) {
        Carrito carrito = validarYObtenerCarrito(usuarioId);
        return carrito.getItems();
    }

    public CarritoItem addItem(Long usuarioId, CarritoItemRequest request) {
        Carrito carrito = validarYObtenerCarrito(usuarioId);

        if (request.getCantidad() <= 0) {
            throw new CantidadInvalidaException();
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(ProductoNotFoundException::new);

        if (!producto.getEstaHabilitado()) {
            throw new ProductoNoHabilitadoException();
        }

        Stock stock = stockRepository.findByProductoId(producto.getId())
                .orElseThrow(StockInsuficienteException::new);

        Optional<CarritoItem> existente = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(request.getProductoId()))
                .findFirst();

        int cantidadTotal = request.getCantidad();
        if (existente.isPresent()) {
            cantidadTotal += existente.get().getCantidad();
        }

        if (stock.getCantidad() < cantidadTotal) {
            throw new StockInsuficienteException();
        }

        CarritoItem item;
        if (existente.isPresent()) {
            item = existente.get();
            item.setCantidad(cantidadTotal);
        } else {
            item = new CarritoItem(carrito, producto, request.getCantidad());
        }

        CarritoItem saved = carritoItemRepository.save(item);
        actualizarModificacion(carrito);
        recalcularSubtotal(carrito);
        return saved;
    }

    public CarritoItem updateCantidad(Long usuarioId, Long productoId, int nuevaCantidad) {
        Carrito carrito = validarYObtenerCarrito(usuarioId);

        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(CarritoItemNotFoundException::new);

        if (nuevaCantidad < 0) {
            throw new CantidadInvalidaException();
        }

        if (nuevaCantidad == 0) {
            removeItem(usuarioId, productoId);
            item.setCantidad(0);
            return item;
        }

        Stock stock = stockRepository.findByProductoId(productoId)
                .orElseThrow(StockInsuficienteException::new);

        if (stock.getCantidad() < nuevaCantidad) {
            throw new StockInsuficienteException();
        }

        item.setCantidad(nuevaCantidad);
        carritoItemRepository.save(item);
        actualizarModificacion(carrito);
        recalcularSubtotal(carrito);
        return item;
    }

    public void removeItem(Long usuarioId, Long productoId) {
        Carrito carrito = validarYObtenerCarrito(usuarioId);

        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(CarritoItemNotFoundException::new);

        carrito.getItems().remove(item);
        carritoRepository.save(carrito);
        actualizarModificacion(carrito);
        recalcularSubtotal(carrito);
    }

    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = validarYObtenerCarrito(usuarioId);

        carrito.getItems().clear();
        carrito.setSubtotal(BigDecimal.ZERO);
        carrito.setUltimaModificacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    // --- Métodos auxiliares privados ---

    private Carrito validarYObtenerCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNotFoundException::new);

        if (usuario.getRol() != RolEnum.CLIENTE) {
            throw new AccesoDenegadoException();
        }

        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(CarritoNotFoundException::new);

        verificarExpiracion(carrito);
        return carrito;
    }

    private void actualizarModificacion(Carrito carrito) {
        carrito.setUltimaModificacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    private void verificarExpiracion(Carrito carrito) {
        if (carrito.getUltimaModificacion().plusDays(DIAS_EXPIRACION).isBefore(LocalDateTime.now())) {
            carrito.getItems().clear();
            carrito.setUltimaModificacion(LocalDateTime.now());
            carrito.setSubtotal(BigDecimal.ZERO);
            carritoRepository.save(carrito);
        }
    }

    private void recalcularSubtotal(Carrito carrito) {
    List<CarritoItem> items = carritoItemRepository.findByCarrito(carrito);

    BigDecimal subtotal = items.stream()
            .map(item -> {
                BigDecimal precio = item.getProducto().getPrecio();
                Optional<Descuento> descuento = descuentoServiceImpl.getDescuentoVigente(item.getProducto().getId());
                if (descuento.isPresent()) {
                    BigDecimal porcentaje = descuento.get().getPorcentaje();
                    BigDecimal factor = BigDecimal.ONE.subtract(porcentaje.divide(BigDecimal.valueOf(100)));
                    precio = precio.multiply(factor);
                }
                return precio.multiply(BigDecimal.valueOf(item.getCantidad()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    carrito.setSubtotal(subtotal);
    carritoRepository.save(carrito);
}
}
