package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ESPECÍFICAS DE USUARIO/NEGOCIO

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