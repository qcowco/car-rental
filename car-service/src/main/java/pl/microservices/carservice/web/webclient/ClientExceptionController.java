package pl.microservices.carservice.web.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.client.ClientException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ClientExceptionController {
    private ObjectMapper objectMapper;

    public ClientExceptionController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = {ClientException.class})
    public void handleUnreachableClient(HttpServletResponse response, RuntimeException exception) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(value = { FeignException.class })
    public void handleFeignExceptions(HttpServletResponse response, FeignException exception) throws IOException {
        String json = exception.contentUTF8();

        JsonNode jsonNode = objectMapper.readTree(json);

        response.sendError(exception.status(), jsonNode.get("message").asText("Response error message."));
    }
}
