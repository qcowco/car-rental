package pl.microservices.rentalrequestservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalrequestservice.model.RentalRequest;
import pl.microservices.rentalrequestservice.service.RentalRequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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
    public Long save(@Valid @RequestBody RentalRequest rentalRequest) {
        if (Objects.nonNull(rentalRequest.getId()))
            throw new IllegalArgumentException("Id needs to be empty");

        return rentalRequestService.save(rentalRequest)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@Valid @RequestBody RentalRequest rentalRequest) {
        if (Objects.isNull(rentalRequest.getId()))
            throw new IllegalArgumentException("Id need not be null while updating");

        rentalRequestService.save(rentalRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalRequestService.deleteById(id);
    }

}
