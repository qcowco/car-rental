package pl.microservices.rentalrequestservice.web.webclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalrequestservice.model.RentalRequest;

import java.time.LocalDate;

@FeignClient(value = "rental-service", path = "/api/v1/rentals")
public interface RentalClient {
    @GetMapping(path = "/{id}", params = { "start", "end" })
    boolean isCarFree(@PathVariable Long id,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end);

    @PostMapping
    Long save(@RequestBody RentalRequest rental);
}
