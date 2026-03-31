package com.justet.shiftlabtest.core.exception;

import com.justet.shiftlabtest.api.error.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorMessage> handleServiceException(ServiceException ex) {

        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(new ErrorMessage(
                        ex.getErrorCode(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(
                        ErrorCode.VALIDATION_ERROR,
                        message
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraint(ConstraintViolationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(
                        ErrorCode.VALIDATION_ERROR,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(
            Exception ex,
            HttpServletRequest request
    ) throws Exception {

        String uri = request.getRequestURI();

        if (uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-ui")) {
            throw ex;
        }

        return ResponseEntity
                .status(ErrorCode.INTERNAL_ERROR.getHttpStatus())
                .body(new ErrorMessage(
                        ErrorCode.INTERNAL_ERROR,
                        ex.getMessage()
                ));
    }
}