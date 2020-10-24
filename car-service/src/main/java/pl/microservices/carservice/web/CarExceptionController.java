package pl.microservices.carservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.microservices.carservice.exception.ResourceNotFound;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CarExceptionController {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void handleIllegalArguments(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(value = {ResourceNotFound.class})
    public void handleNotFound(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
