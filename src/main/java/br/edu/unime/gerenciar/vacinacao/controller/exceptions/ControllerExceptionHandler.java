package br.edu.unime.gerenciar.vacinacao.controller.exceptions;

import br.edu.unime.gerenciar.vacinacao.service.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.lang.ClassCastException;
import java.time.Instant;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error. Check 'errors' field for details.");
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        final String errorMessage = "Ocorreu um erro na aplicação. Nossa equipe deTI já foi notificada e em breve nossos serviços estarão reestabelecidos.Para maiores informações entre em contato pelo nosso WhatsApp 71 99999-9999. Lamentamos o ocorrido!";
        log.error(errorMessage, exception);
        return buildErrorResponse(exception, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        StandardError errorResponse = new StandardError("Resource Not Found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> handleBadRequestException(BadRequestException ex) {
        StandardError errorResponse = new StandardError("Bad Request", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //@ExceptionHandler Dessa forma, “avisamos” ao Spring que esta função é um tratamento de exceção
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> EntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        //err.setError("EntityNotFoundException!");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<StandardError> ClassCastException(ClassCastException e, HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        //err.setError(e.getMessage());
        err.setMessage("Registro do Paciente Não Localizado!");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardError> NullPointerException(NullPointerException e, HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        //err.setError("NullPointerException!");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> IllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        //err.setError("IllegalArgumentException!");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException, WebRequest request) {
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
        return buildErrorResponse(dataIntegrityViolationException, errorMessage, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException constraintViolationException, WebRequest request) {
        log.error("Failed to validate element", constraintViolationException);
        return buildErrorResponse(constraintViolationException, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException objectNotFoundException, WebRequest request) {
        log.error("Failed to find the requested element", objectNotFoundException);
        return buildErrorResponse(objectNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(DataBindingViolationException dataBindingViolationException, WebRequest request) {
        log.error("Failed to save entity with associated data", dataBindingViolationException);
        return buildErrorResponse(dataBindingViolationException, HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        //err.setError("MethodArgumentTypeMismatchException!");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}