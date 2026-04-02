package com.justet.shiftlabtest.api.error;

import com.justet.shiftlabtest.core.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleServiceException_shouldReturnCorrectResponse() {

        ServiceException ex = new ServiceException(
                ErrorCode.SELLER_NOT_FOUND,
                "Seller not found"
        );

        ResponseEntity<ErrorMessage> response =
                handler.handleServiceException(ex);

        assertThat(response.getStatusCode())
                .isEqualTo(ErrorCode.SELLER_NOT_FOUND.getHttpStatus());

        ErrorMessage body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.code()).isEqualTo(ErrorCode.SELLER_NOT_FOUND);
        assertThat(body.message()).isEqualTo("Seller not found");
    }

    @Test
    void handleValidation_shouldReturnFirstFieldError() {

        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "object");

        bindingResult.addError(new FieldError(
                "object",
                "name",
                "must not be blank"
        ));

        MethodParameter parameter = mock(MethodParameter.class);

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorMessage> response =
                handler.handleValidation(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);

        ErrorMessage body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.code()).isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThat(body.message()).contains("name");
    }

    @Test
    void handleConstraint_shouldReturnBadRequest() {

        ConstraintViolationException ex =
                new ConstraintViolationException("Invalid value", Collections.emptySet());

        ResponseEntity<ErrorMessage> response =
                handler.handleConstraint(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);

        ErrorMessage body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.code()).isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThat(body.message()).contains("Invalid value");
    }

    @Test
    void handleOther_shouldReturnInternalError() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");

        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<?> response =
                handler.handleOther(ex, request);

        assertThat(response.getStatusCode())
                .isEqualTo(ErrorCode.INTERNAL_ERROR.getHttpStatus());

        ErrorMessage body = (ErrorMessage) response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.code()).isEqualTo(ErrorCode.INTERNAL_ERROR);
        assertThat(body.message()).contains("Something went wrong");
    }

    @Test
    void handleOther_shouldRethrowForSwagger() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        Exception ex = new RuntimeException("Swagger error");

        assertThatThrownBy(() ->
                handler.handleOther(ex, request)
        ).isEqualTo(ex);
    }
}