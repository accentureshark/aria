package com.accenture.aria.controller;

import com.accenture.aria.dto.ApiErrorResponseDTO;
import com.accenture.aria.dto.FieldValidationErrorDTO;
import com.accenture.aria.service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        List<FieldValidationErrorDTO> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldValidationError)
                .toList();

        ApiErrorResponseDTO response = buildError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleNotFound(ResourceNotFoundException ex,
                                                              HttpServletRequest request) {
        ApiErrorResponseDTO response = buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleConflict(HttpServletRequest request) {
        ApiErrorResponseDTO response = buildError(
                HttpStatus.CONFLICT,
                "Data integrity violation",
                request.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleBadRequest(IllegalArgumentException ex,
                                                                HttpServletRequest request) {
        ApiErrorResponseDTO response = buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUnexpected(HttpServletRequest request) {
        ApiErrorResponseDTO response = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                request.getRequestURI(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private FieldValidationErrorDTO toFieldValidationError(FieldError fieldError) {
        return FieldValidationErrorDTO.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    private ApiErrorResponseDTO buildError(HttpStatus status,
                                           String message,
                                           String path,
                                           List<FieldValidationErrorDTO> fieldErrors) {
        return ApiErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }
}

