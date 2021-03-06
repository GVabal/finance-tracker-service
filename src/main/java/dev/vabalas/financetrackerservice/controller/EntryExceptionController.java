package dev.vabalas.financetrackerservice.controller;

import dev.vabalas.financetrackerservice.exception.EntryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class EntryExceptionController {
    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntryNotFoundException(EntryNotFoundException exception) {
        log.info("EntryNotFoundException handled: {}", exception.getMessage());
        return generateResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();
        log.info("MethodArgumentNotValidException handled: {}", message);
        return generateResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleInternalServerError(Exception exception) {
        log.error("Exception handled: {}", exception.getMessage());
        return generateResponse("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> generateResponseBody(HttpStatus status, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("code", Integer.toString(status.value()));
        map.put("error", status.getReasonPhrase());
        map.put("message", message);
        map.put("timestamp", LocalDateTime.now().toString());
        return map;
    }

    private ResponseEntity<Map<String, String>> generateResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(generateResponseBody(status, message), status);
    }
}
