package br.edu.unime.gerenciar.vacinacao.controller.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    //StandardError
    private List<ValidationError> errors;
    private Instant timestamp;
    private static String error;
    private String path;

    @Data
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }

    /*
        public String toJson() {
            return "{\"status\": " + getStatus() + ", " +
                    "\"message\": \"" + getMessage() + "\"}";
        }

     */

    public String toJson() {
        return "{\"status\": " + getStatus() + ", " +
                "\"path\": " + getPath() + ", " +
                "\"timestamp\": " + getTimestamp() + ", " +
                "\"message\": \"" + getMessage() + "\"}";
    }

}
