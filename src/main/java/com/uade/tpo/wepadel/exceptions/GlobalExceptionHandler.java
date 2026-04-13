package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ESPECÍFICAS DE USUARIO

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Object> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }

    @ExceptionHandler(UsuarioDuplicateException.class)
    public ResponseEntity<Object> handleUsuarioDuplicate(UsuarioDuplicateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El mail ya está registrado");
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Object> handleAccesoDenegado(AccesoDenegadoException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "No tenés permiso para realizar esta acción");
    }

    // ESPECÍFICAS DE CARRITO

    @ExceptionHandler(CarritoNotFoundException.class)
    public ResponseEntity<Object> handleCarritoNotFound(CarritoNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Carrito no encontrado para este usuario");
    }

    @ExceptionHandler(CarritoItemNotFoundException.class)
    public ResponseEntity<Object> handleCarritoItemNotFound(CarritoItemNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "El producto no está en el carrito");
    }

    @ExceptionHandler(CantidadInvalidaException.class)
    public ResponseEntity<Object> handleCantidadInvalida(CantidadInvalidaException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
    }

    // ESPECÍFICAS DE PRODUCTO/STOCK

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Object> handleProductoNotFound(ProductoNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Producto no encontrado");
    }

    @ExceptionHandler(ProductoNoHabilitadoException.class)
    public ResponseEntity<Object> handleProductoNoHabilitado(ProductoNoHabilitadoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El producto no está disponible para la venta");
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Object> handleStockInsuficiente(StockInsuficienteException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Stock insuficiente para la cantidad solicitada");
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
        return buildResponse(HttpStatus.BAD_REQUEST, "Error en el formato del JSON o valor de Rol inválido");
    }

    @ExceptionHandler(RuntimeException.class) // Usada para validaciones manuales de mail/pass
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