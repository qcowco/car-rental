package pl.microservices.userservice.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ValidationExceptionDetails {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private List<String> errors;
    private String message;
    private String path;

    public ValidationExceptionDetails(int status, List<String> errors, String message, String path) {
        this.status = status;
        this.errors = errors;
        this.message = message;
        this.path = path;
    }
}
