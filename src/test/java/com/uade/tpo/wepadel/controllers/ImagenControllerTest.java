package com.uade.tpo.wepadel.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.uade.tpo.wepadel.entity.dto.ImagenArchivo;
import com.uade.tpo.wepadel.service.ImagenService;

@WebMvcTest(ImagenController.class)
@AutoConfigureMockMvc(addFilters = false)
class ImagenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImagenService imagenService;

    @Test
    void getImagenArchivo_devuelveBytesConContentTypeImagen() throws Exception {
        byte[] bytes = new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47 };
        when(imagenService.getImagenArchivo(5L))
                .thenReturn(new ImagenArchivo(bytes, "foto.png", "image/png"));

        mockMvc.perform(get("/imagenes/5/archivo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"foto.png\""))
                .andExpect(content().bytes(bytes));
    }

}
