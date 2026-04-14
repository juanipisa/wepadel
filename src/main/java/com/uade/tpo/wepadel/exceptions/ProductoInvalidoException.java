package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProductoInvalidoException extends RuntimeException {
    public ProductoInvalidoException(String message) {
        super(message);
    }
}