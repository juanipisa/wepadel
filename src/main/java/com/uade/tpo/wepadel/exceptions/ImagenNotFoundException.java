package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ImagenNotFoundException extends RuntimeException {
    public ImagenNotFoundException() {
        super();
    }
}
