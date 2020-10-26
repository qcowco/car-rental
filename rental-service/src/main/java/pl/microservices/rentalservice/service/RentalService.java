package pl.microservices.rentalservice.service;

import org.springframework.data.domain.Page;
import pl.microservices.rentalservice.model.Rental;

import java.time.LocalDate;

public interface RentalService {
    Iterable<Rental> findAll();

    Page<Rental> findAll(int page, int size);

    Rental findById(Long id);

    Rental save(Rental rental);

    void deleteById(Long id);

    boolean isCarFree(Long id, LocalDate start, LocalDate end);
}
