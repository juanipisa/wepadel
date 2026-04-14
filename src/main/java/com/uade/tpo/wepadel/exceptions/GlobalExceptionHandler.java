package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // PRODUCTO

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Object> handleProductoNotFound(ProductoNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Producto no encontrado");
    }

    @ExceptionHandler(ProductoInvalidoException.class)
    public ResponseEntity<Object> handleProductoInvalido(ProductoInvalidoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // STOCK

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Object> handleStockNotFound(StockNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Stock no encontrado para el producto");
    }

    @ExceptionHandler(StockNegativoException.class)
    public ResponseEntity<Object> handleStockNegativo(StockNegativoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
    }

    // USUARIO

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Object> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }

    @ExceptionHandler(UsuarioDuplicateException.class)
    public ResponseEntity<Object> handleUsuarioDuplicate(UsuarioDuplicateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El mail ya está registrado");
    }

    @ExceptionHandler(CategoriaDuplicateException.class)
    public ResponseEntity<Object> handleCategoriaDuplicate(CategoriaDuplicateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "La categoría ya existe");
    }

    // GENÉRICAS DEL SISTEMA

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowed(org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no existe en este controller.");
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonError(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Error en el formato del JSON o valor de enum inválido");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // AUX
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}