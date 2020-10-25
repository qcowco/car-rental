package pl.microservices.rentalrequestservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalrequestservice.model.RentalRequest;
import pl.microservices.rentalrequestservice.service.RentalRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/rental-requests")
public class RentalRequestController {
    private RentalRequestService rentalRequestService;

    public RentalRequestController(RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    @GetMapping
    public Iterable<RentalRequest> findAll() {
        return rentalRequestService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<RentalRequest> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalRequestService.findAll(page, size)
                .getContent();
    }

    @GetMapping("/{id}")
    public RentalRequest findById(@PathVariable Long id) {
        return rentalRequestService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody RentalRequest rentalRequest) {
        return rentalRequestService.save(rentalRequest)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestBody RentalRequest rentalRequest) {
        rentalRequestService.save(rentalRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalRequestService.deleteById(id);
    }

}
