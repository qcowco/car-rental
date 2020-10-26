package pl.microservices.rentalrequestservice.web.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class FeignExceptionController {
    private ObjectMapper objectMapper;

    public FeignExceptionController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = { FeignException.class })
    public void handleFeignExceptions(HttpServletResponse response, FeignException exception) throws IOException {
        String json = exception.contentUTF8();

        JsonNode jsonNode = objectMapper.readTree(json);

        response.sendError(exception.status(), jsonNode.get("message").asText("Response error message."));
    }
}
