package com.uade.tpo.wepadel.service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.CategoriaEnum;
import com.uade.tpo.wepadel.entity.Imagen;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.CategoriaResponse;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;
import com.uade.tpo.wepadel.repository.ImagenRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    public List<CategoriaResponse> listar() {
        return Arrays.stream(CategoriaEnum.values())
                .map(this::toResponse)
                .toList();
    }

    private CategoriaResponse toResponse(CategoriaEnum categoria) {
        return CategoriaResponse.builder()
                .valor(categoria.name())
                .slug(categoria.name().toLowerCase())
                .portada(resolverPortada(categoria))
                .build();
    }

    private ImagenResponse resolverPortada(CategoriaEnum categoria) {
        List<Producto> productos = productoRepository
                .findByCategoriaAndEstaHabilitadoTrueOrderByIdAsc(categoria);

        for (Producto producto : productos) {
            List<Imagen> imagenes = imagenRepository.findByProductoIdOrderByIdAsc(producto.getId());
            if (imagenes.isEmpty()) {
                continue;
            }

            Imagen imagen = imagenes.get(0);
            byte[] contenido = imagen.getContenido();
            if (contenido == null || contenido.length == 0) {
                continue;
            }

            return ImagenResponse.builder()
                    .id(imagen.getId())
                    .nombre(imagen.getNombre())
                    .archivoBase64(Base64.getEncoder().encodeToString(contenido))
                    .build();
        }

        return null;
    }

}
