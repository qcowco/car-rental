package pl.microservices.carservice.web.webclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "rental-service", path = "/api/v1/rentals")
public interface RentalClient {
    @GetMapping("/{id}/isFree")
    boolean isCarFreeInFuture(@PathVariable Long id);
}
