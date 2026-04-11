package com.uade.tpo.wepadel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El mail ya está registrado")
public class UsuarioDuplicateException extends Exception {

}
