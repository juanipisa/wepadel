package com.uade.tpo.wepadel.entity.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddImagenRequest {

    private MultipartFile archivo;
    private Long productoId;

}
