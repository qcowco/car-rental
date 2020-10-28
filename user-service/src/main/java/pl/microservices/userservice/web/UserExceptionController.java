package pl.microservices.userservice.web;

import org.keycloak.authorization.client.util.HttpResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class UserExceptionController {
    @ExceptionHandler(value = {HttpResponseException.class})
    public void handleHttpResponseException(HttpServletResponse response, HttpResponseException exception) throws IOException {
        String message = exception.getReasonPhrase();

        if (exception.getStatusCode() == 401)
            message = "Incorrect credentials";

        response.sendError(exception.getStatusCode(), message);
    }


}
