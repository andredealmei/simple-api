package br.com.andredealmei.handler;

import br.com.andredealmei.error.ResourceNotFoundDetails;
import br.com.andredealmei.error.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestEcxeptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {

        ResourceNotFoundDetails resourceNotFoundDetails  = ResourceNotFoundDetails.builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .details(e.getMessage())
                .developerMessage(e.getClass().getName())
                .build();
        return  new ResponseEntity<>(resourceNotFoundDetails,HttpStatus.NOT_FOUND);

    }





}
