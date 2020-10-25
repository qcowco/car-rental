package pl.microservices.rentalrequestservice.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalrequestservice.dao.RentalRequestRepository;
import pl.microservices.rentalrequestservice.model.RentalRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/rental-requests")
public class RentalRequestController {
    private RentalRequestRepository rentalRequestRepository;

    public RentalRequestController(RentalRequestRepository rentalRequestRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @GetMapping
    public Iterable<RentalRequest> findAll() {
        return rentalRequestRepository.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<RentalRequest> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalRequestRepository.findAll(PageRequest.of(page, size))
                .getContent();
    }

    @GetMapping("/{id}")
    public RentalRequest findById(@PathVariable Long id) {
        return rentalRequestRepository.findById(id)
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody RentalRequest rentalRequest) {
        return rentalRequestRepository.save(rentalRequest)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestBody RentalRequest rentalRequest) {
        rentalRequestRepository.save(rentalRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalRequestRepository.deleteById(id);
    }

}
