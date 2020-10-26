package pl.microservices.rentalrequestservice.web.webclient;

import feign.FeignException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class FeignExceptionController {

    @ExceptionHandler(value = { FeignException.class })
    public void handleFeignExceptions(HttpServletResponse response, FeignException exception) throws IOException {
        response.sendError(exception.status(), exception.getMessage());
    }
}
