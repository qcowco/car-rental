package pl.microservices.rentalservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalservice.model.Rental;
import pl.microservices.rentalservice.service.RentalService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class RentalController {
    private RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public Iterable<Rental> findAll() {
        return rentalService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<Rental> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalService.findAll(page, size)
                .getContent();
    }

    @GetMapping("/{id}")
    public Rental findById(@PathVariable Long id) {
        return rentalService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@Valid @RequestBody Rental rental) {
        if (Objects.nonNull(rental.getId()))
            throw new IllegalArgumentException("Id needs to be empty");

        return rentalService.save(rental)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@Valid @RequestBody Rental rental) {
        if (Objects.isNull(rental.getId()))
            throw new IllegalArgumentException("Id need not be null while updating");

        rentalService.save(rental);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalService.deleteById(id);
    }

    // TODO: 25.10.2020 znajdz wolne fury w danym terminie
}
