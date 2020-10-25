package pl.microservices.rentalservice.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalservice.dao.RentalRepository;
import pl.microservices.rentalservice.model.Rental;

import java.util.List;

@RestController
public class RentalController {
    private RentalRepository rentalRepository;

    public RentalController(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @GetMapping
    public Iterable<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<Rental> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalRepository.findAll(PageRequest.of(page, size))
                .getContent();
    }

    @GetMapping("/{id}")
    public Rental findById(@PathVariable Long id) {
        return rentalRepository.findById(id)
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody Rental rental) {
        return rentalRepository.save(rental)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestBody Rental rental) {
        rentalRepository.save(rental);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalRepository.deleteById(id);
    }

    // TODO: 25.10.2020 znajdz wolne fury w danym terminie
}
