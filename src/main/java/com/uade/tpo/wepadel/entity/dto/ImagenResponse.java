package com.uade.tpo.wepadel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenResponse {

    private Long id;
    private String nombre;
    private String archivoBase64;

}
