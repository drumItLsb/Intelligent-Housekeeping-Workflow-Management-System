package com.kickdrum.HousekeepingWorkflowManagementSystem.exception;

import com.kickdrum.HousekeepingWorkflowManagementSystem.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExists(
            ResourceAlreadyExistsException ex,
            HttpServletRequest req
    ) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
