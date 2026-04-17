package com.uade.tpo.wepadel.service;

import java.util.List;

import com.uade.tpo.wepadel.entity.dto.AddImagenRequest;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;

public interface ImagenService {

    ImagenResponse getImagenById(Long id);

    Long createImagen(AddImagenRequest request);

    List<ImagenResponse> getImagenesByProductoId(Long productoId);
}
