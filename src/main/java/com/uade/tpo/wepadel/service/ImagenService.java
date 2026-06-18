package com.uade.tpo.wepadel.service;

import java.util.List;

import com.uade.tpo.wepadel.entity.dto.AddImagenRequest;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;
import com.uade.tpo.wepadel.entity.dto.UpdateImagenRequest;

public interface ImagenService {

    ImagenResponse getImagenById(Long id);

    Long createImagen(AddImagenRequest request);

    void updateImagen(Long imagenId, UpdateImagenRequest request);

    List<ImagenResponse> getImagenesByProductoId(Long productoId);
}
