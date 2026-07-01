package com.uade.tpo.wepadel.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.uade.tpo.wepadel.entity.CategoriaEnum;
import com.uade.tpo.wepadel.entity.dto.ImagenCatalogoResponse;
import com.uade.tpo.wepadel.entity.dto.ProductoEnrichedResponse;
import com.uade.tpo.wepadel.service.ImagenService;
import com.uade.tpo.wepadel.service.ProductoService;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ImagenService imagenService;

    @Test
    void getProductos_devuelveListaEnriched() throws Exception {
        ProductoEnrichedResponse enriched = ProductoEnrichedResponse.builder()
                .id(1L)
                .nombre("Paleta")
                .descripcion("Desc")
                .precio(BigDecimal.TEN)
                .categoria(CategoriaEnum.PALETAS)
                .estaHabilitado(true)
                .stock(5)
                .imagenPrincipal(ImagenCatalogoResponse.builder()
                        .id(5L)
                        .nombre("foto.png")
                        .url("/imagenes/5/archivo")
                        .build())
                .descuentos(List.of())
                .build();
        when(productoService.getProductosEnriched(false)).thenReturn(List.of(enriched));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].stock").value(5))
                .andExpect(jsonPath("$[0].imagenPrincipal.url").value("/imagenes/5/archivo"))
                .andExpect(jsonPath("$[0].descuentos").isArray());

        verify(productoService).getProductosEnriched(eq(false));
    }

}
