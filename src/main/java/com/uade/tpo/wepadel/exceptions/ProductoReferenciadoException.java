package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ProductoReferenciadoException extends RuntimeException {
    public ProductoReferenciadoException(String message) {
        super(message);
    }
}
