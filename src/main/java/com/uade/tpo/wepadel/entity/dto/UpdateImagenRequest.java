package com.uade.tpo.wepadel.entity.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateImagenRequest {

    @NotNull
    private MultipartFile archivo;

}
