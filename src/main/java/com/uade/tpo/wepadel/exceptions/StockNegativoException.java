package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El stock no puede ser negativo")
public class StockNegativoException extends RuntimeException {
    public StockNegativoException() { super(); }
}
