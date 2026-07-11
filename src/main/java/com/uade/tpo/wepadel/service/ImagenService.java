package com.uade.tpo.wepadel.service;

import java.util.List;

import com.uade.tpo.wepadel.entity.dto.AddImagenRequest;
import com.uade.tpo.wepadel.entity.dto.ImagenArchivo;
import com.uade.tpo.wepadel.entity.dto.ImagenCatalogoResponse;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;
import com.uade.tpo.wepadel.entity.dto.UpdateImagenRequest;

public interface ImagenService {

    String URL_ARCHIVO_PREFIX = "/imagenes/";

    ImagenResponse getImagenById(Long id);

    ImagenArchivo getImagenArchivo(Long id);

    static String urlArchivo(Long imagenId) {
        return URL_ARCHIVO_PREFIX + imagenId + "/archivo";
    }

    ImagenCatalogoResponse createImagen(AddImagenRequest request);

    ImagenCatalogoResponse updateImagen(Long imagenId, UpdateImagenRequest request);

    List<ImagenResponse> getImagenesByProductoId(Long productoId);
}
