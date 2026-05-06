
package com.gui.estoqueAPI.Controller.Exceptions;

import com.gui.estoqueAPI.Service.Exception.BadRequestException;
import com.gui.estoqueAPI.Service.Exception.BadRequestExceptionQuantidade;
import com.gui.estoqueAPI.Service.Exception.ResourceNotFoundExceptionEstoque;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<StandardError> badRequest(BadRequestException exception, HttpServletRequest request){
        String error = "SKU informado é inválido";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(),request.getRequestURI(), exception.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({ResourceNotFoundExceptionEstoque.class})
    public ResponseEntity<StandardError> resourceNotFoundEstoque(ResourceNotFoundExceptionEstoque exception, HttpServletRequest request){
        String error = "SKU informado é inválido";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), request.getRequestURI(), exception.getMessage(),error,status.value());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler({BadRequestExceptionQuantidade.class})
    public ResponseEntity<StandardError> badRequestQuantidade(BadRequestExceptionQuantidade exception, HttpServletRequest request){
        String error = "Estoque insuficiente para a quantidade desejada";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), request.getRequestURI(), exception.getMessage(), error,status.value());
        return ResponseEntity.status(status).body(err);
    }

}
