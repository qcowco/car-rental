package pl.microservices.rentalrequestservice.web.webclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.microservices.rentalrequestservice.web.webclient.dto.Car;

@FeignClient(value = "car-service", path = "/api/v1/cars")
public interface CarClient {
    @GetMapping("/{id}")
    Car findById(@PathVariable Long id);
}
