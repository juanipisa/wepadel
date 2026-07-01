package com.uade.tpo.wepadel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uade.tpo.wepadel.entity.CategoriaEnum;
import com.uade.tpo.wepadel.entity.Descuento;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.Stock;
import com.uade.tpo.wepadel.entity.dto.ProductoEnrichedResponse;
import com.uade.tpo.wepadel.repository.CarritoItemRepository;
import com.uade.tpo.wepadel.repository.DescuentoRepository;
import com.uade.tpo.wepadel.repository.ImagenMetadataProjection;
import com.uade.tpo.wepadel.repository.ImagenRepository;
import com.uade.tpo.wepadel.repository.OrdenItemRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;
import com.uade.tpo.wepadel.repository.StockRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private OrdenItemRepository ordenItemRepository;

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private DescuentoRepository descuentoRepository;

    @Mock
    private ImagenRepository imagenRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void getProductosEnriched_agrupaStockImagenPrincipalYDescuentosEnBatch() {
        Producto producto = new Producto("Paleta", "Desc", BigDecimal.valueOf(100), CategoriaEnum.PALETAS);
        producto.setId(1L);

        Stock stock = new Stock(producto, 12);
        ImagenMetadataProjection imagenMetadata = metadata(5L, "foto.png", 1L);
        Descuento descuento = new Descuento(
                producto,
                BigDecimal.TEN,
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 23, 59));
        descuento.setId(2L);

        when(productoRepository.findByEstaHabilitadoTrue()).thenReturn(List.of(producto));
        when(stockRepository.findByProductoIdIn(List.of(1L))).thenReturn(List.of(stock));
        when(imagenRepository.findMetadataByProductoIdIn(List.of(1L))).thenReturn(List.of(imagenMetadata));
        when(descuentoRepository.findByProductoIdIn(List.of(1L))).thenReturn(List.of(descuento));

        List<ProductoEnrichedResponse> result = productoService.getProductosEnriched(false);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStock()).isEqualTo(12);
        assertThat(result.get(0).getImagenPrincipal()).isNotNull();
        assertThat(result.get(0).getImagenPrincipal().getNombre()).isEqualTo("foto.png");
        assertThat(result.get(0).getImagenPrincipal().getUrl()).isEqualTo("/imagenes/5/archivo");
        assertThat(result.get(0).getDescuentos()).hasSize(1);
        assertThat(result.get(0).getDescuentos().get(0).getPorcentaje()).isEqualByComparingTo(BigDecimal.TEN);

        verify(stockRepository).findByProductoIdIn(anyCollection());
        verify(imagenRepository).findMetadataByProductoIdIn(anyCollection());
        verify(descuentoRepository).findByProductoIdIn(anyCollection());
    }

    @Test
    void getProductosEnriched_listaVacia_noConsultaRelaciones() {
        when(productoRepository.findByEstaHabilitadoTrue()).thenReturn(List.of());

        List<ProductoEnrichedResponse> result = productoService.getProductosEnriched(false);

        assertThat(result).isEmpty();
    }

    private static ImagenMetadataProjection metadata(Long id, String nombre, Long productoId) {
        return new ImagenMetadataProjection() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public String getNombre() {
                return nombre;
            }

            @Override
            public Long getProductoId() {
                return productoId;
            }
        };
    }

}
