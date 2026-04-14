package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El stock no existe")
public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException() { super(); }

}
