package br.com.andredealmei.handler;

import br.com.andredealmei.error.ErrorDetails;
import br.com.andredealmei.error.ResourceNotFoundDetails;
import br.com.andredealmei.error.ResourceNotFoundException;
import br.com.andredealmei.error.ValidationErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

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
        return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.NOT_FOUND);
    }


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String defaultMessageErrors = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));


        ValidationErrorDetails resourceNotFoundDetails = ValidationErrorDetails.builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field Validation error")
                .details("Field validation error")
                .developerMessage(e.getClass().getName())
                .field(fields)
                .fieldMessage(defaultMessageErrors)
                .build();
        return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.BAD_REQUEST);

    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetails errorDetails = ErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Internal Exception")
                .details(e.getMessage())
                .developerMessage(e.getClass().getName())
                .build();
        return new ResponseEntity<>(errorDetails, status);




    }
}
