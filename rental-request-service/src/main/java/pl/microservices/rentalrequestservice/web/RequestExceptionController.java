package pl.microservices.rentalrequestservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.microservices.rentalrequestservice.exception.ResourceNotFoundException;
import pl.microservices.rentalrequestservice.exception.UnauthorizedRequestException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RequestExceptionController {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void handleIllegalArguments(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public void handleNotFound(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(value = {UnauthorizedRequestException.class})
    public void handleUnauthorized(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
    }
}
