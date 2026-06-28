package com.uade.tpo.wepadel.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.wepadel.entity.CategoriaEnum;
import com.uade.tpo.wepadel.entity.Imagen;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.CategoriaResponse;
import com.uade.tpo.wepadel.repository.ImagenRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;
import com.uade.tpo.wepadel.util.ImagenDataUrlUtil;

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
        String id = categoria.name().toLowerCase(Locale.ROOT);

        return CategoriaResponse.builder()
                .id(id)
                .label(categoria.name())
                .img(resolverPortada(categoria))
                .path(resolverPath(id))
                .build();
    }

    private String resolverPath(String id) {
        return "/catalogo/" + id;
    }

    private String resolverPortada(CategoriaEnum categoria) {
        List<Producto> productos = productoRepository
                .findByCategoriaAndEstaHabilitadoTrueOrderByIdAsc(categoria);

        for (Producto producto : productos) {
            List<Imagen> imagenes = imagenRepository.findByProductoIdOrderByIdAsc(producto.getId());
            if (imagenes.isEmpty()) {
                continue;
            }

            String dataUrl = ImagenDataUrlUtil.toDataUrl(imagenes.get(0));
            if (dataUrl != null) {
                return dataUrl;
            }
        }

        return null;
    }

}
