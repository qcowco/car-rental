package pl.microservices.userservice.web;

import org.keycloak.authorization.client.util.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.microservices.userservice.web.dto.ValidationExceptionDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class UserExceptionController {
    @ExceptionHandler(value = {HttpResponseException.class})
    public void handleHttpResponseException(HttpServletResponse response, HttpResponseException exception) throws IOException {
        String message = exception.getReasonPhrase();

        if (exception.getStatusCode() == 401)
            message = "Incorrect credentials";

        response.sendError(exception.getStatusCode(), message);
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationExceptionDetails> handleMethodArgumentNotValid(HttpServletRequest request,
                                                                                   MethodArgumentNotValidException exception) {
        List<String> errors = getErrors(exception);

        ValidationExceptionDetails exceptionDetails = new ValidationExceptionDetails(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                "Validation errors",
                request.getServletPath()
        );

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    private List<String> getErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        BindingResult bindingResult = ex.getBindingResult();

        bindingResult.getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .forEach(errors::add);

        bindingResult.getGlobalErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getObjectName(), error.getDefaultMessage()))
                .forEach(errors::add);

        return errors;
    }
}
