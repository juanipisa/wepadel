package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(UsuarioDatosInvalidosException.class)
    public ResponseEntity<Object> handleUsuarioDatosInvalidos(UsuarioDatosInvalidosException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");
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
    
    @ExceptionHandler(CarritoVacioException.class)
    public ResponseEntity<Object> handleCarritoVacio(CarritoVacioException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El carrito está vacío");
    }

    // ESPECÍFICAS DE PRODUCTO/STOCK

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Object> handleProductoNotFound(ProductoNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Producto no encontrado");
    }

    @ExceptionHandler(ImagenNotFoundException.class)
    public ResponseEntity<Object> handleImagenNotFound(ImagenNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "No hay imagen con el id proporcionado");
    }

    @ExceptionHandler(ProductoInvalidoException.class)
    public ResponseEntity<Object> handleProductoInvalido(ProductoInvalidoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
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

    // ÓRDENES

    @ExceptionHandler(OrdenNotFoundException.class)
    public ResponseEntity<Object> handleOrdenNotFound(OrdenNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Orden no encontrada");
    }

    @ExceptionHandler(OrdenCantBeCancelledException.class)
    public ResponseEntity<Object> handleOrdenCantBeCancelled(OrdenCantBeCancelledException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PuntosCanjeInvalidoException.class)
    public ResponseEntity<Object> handlePuntosCanjeInvalido(PuntosCanjeInvalidoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Si usás puntos, debés indicar puntosUsados mayor a cero");
    }

    // GENÉRICAS DEL SISTEMA

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(this::mensajeValidacionEntrada)
                .orElse("Datos de entrada inválidos");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowed(org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED,
                "El método " + ex.getMethod() + " no existe en este controller.");
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonError(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Error en el formato del JSON o valor de enum inválido");
    }

    // DESCUENTO
    
    @ExceptionHandler(DescuentoNotFoundException.class)
    public ResponseEntity<Object> handleDescuentoNotFound(DescuentoNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Descuento no encontrado");
    }

    // SISTEMA DE PUNTOS
    
    @ExceptionHandler(SistemaPuntosNotFoundException.class)
    public ResponseEntity<Object> handleSistemaPuntosNotFound(SistemaPuntosNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "El usuario no tiene sistema de puntos inicializado");
    }

    @ExceptionHandler(PuntosInsuficientesException.class)
    public ResponseEntity<Object> handlePuntosInsuficientes(PuntosInsuficientesException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "El usuario no tiene suficientes puntos");
    }

    @ExceptionHandler(PuntosNegativosException.class)
    public ResponseEntity<Object> handlePuntosNegativos(PuntosNegativosException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "La cantidad de puntos no puede ser negativa");
    }

    // Para errores no contemplados 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnhandled(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    // Para errores de datos de entrada
    private String mensajeValidacionEntrada(ObjectError error) {
        if (!(error instanceof FieldError fe)) {
            return error.getDefaultMessage() != null ? error.getDefaultMessage() : "Datos de entrada inválidos";
        }
        String field = fe.getField();
        
        // RegisterRequest
        if ("nombreApellido".equals(field) && matchesConstraint(fe, "NotBlank")) {
            return "El nombre y apellido es obligatorio";
        }
        if ("email".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El email es obligatorio";
        }
        if ("email".equals(field) && matchesConstraint(fe, "Email")) {
            return "El formato del email no es válido";
        }
        if ("password".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "La contraseña es obligatoria";
        }
        if ("password".equals(field) && matchesConstraint(fe, "Size")) {
            return "La contraseña debe tener al menos 12 caracteres";
        }
        if ("password".equals(field) && matchesConstraint(fe, "Pattern")) {
            return "La contraseña debe tener 12+ caracteres, una mayúscula, un número y un símbolo";
        }
        if ("role".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El rol es obligatorio";
        }
        
        // DescuentoRequest
        if ("productoId".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El producto es obligatorio";
        }
        if ("porcentaje".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El porcentaje es obligatorio";
        }
        if ("porcentaje".equals(field) && matchesConstraint(fe, "DecimalMin")) {
            return "El porcentaje debe ser mayor a 0";
        }
        if ("fechaInicio".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "La fecha de inicio es obligatoria";
        }
        if ("fechaFin".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "La fecha de fin es obligatoria";
        }
        
        // CarritoItemRequest
        if ("productoId".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El producto es obligatorio";
        }
        if ("cantidad".equals(field) && matchesConstraint(fe, "Min")) {
            return "La cantidad debe ser mayor a 0";
        }
        
        // ProductoRequest
        if ("descripcion".equals(field) && matchesConstraint(fe, "NotBlank")) {
            return "La descripción es obligatoria";
        }
        if ("precio".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El precio es obligatorio";
        }
        if ("precio".equals(field) && matchesConstraint(fe, "DecimalMin")) {
            return "El precio debe ser mayor a 0";
        }
    
        // OrdenRequest
        if ("usuario".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "El campo usuario es obligatorio";
        }
        if ("direccion".equals(field) && matchesConstraint(fe, "NotBlank")) {
            return "La dirección es obligatoria";
        }
        if ("cp".equals(field) && matchesConstraint(fe, "NotBlank")) {
            return "El código postal es obligatorio";
        }
        if ("montoEnvio".equals(field) && matchesConstraint(fe, "NotNull")) {
            return "Solicitud incompleta: falta montoEnvio en el payload (debe calcularse y enviarse antes de confirmar la orden)";
        }
        if ("montoEnvio".equals(field) && matchesConstraint(fe, "DecimalMin")) {
            return "montoEnvio inválido: el valor calculado no puede ser negativo";
        }
        return error.getDefaultMessage() != null ? error.getDefaultMessage() : "Datos de entrada inválidos";
    }

    private boolean matchesConstraint(FieldError fe, String suffix) {
        for (String code : fe.getCodes()) {
            if (code != null && code.endsWith(suffix)) {
                return true;
            }
        }
        return false;
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