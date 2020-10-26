package pl.microservices.rentalservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.microservices.rentalservice.dao.RentalRepository;
import pl.microservices.rentalservice.exception.ResourceNotFoundException;
import pl.microservices.rentalservice.model.Rental;

import java.time.LocalDate;

@Service
public class RentalServiceImpl implements RentalService {
    private RentalRepository rentalRepository;

    public RentalServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Iterable<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @Override
    public Page<Rental> findAll(int page, int size) {
        return rentalRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Rental findById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Rental with id %d not found", id)));
    }

    @Override
    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public void deleteById(Long id) {
        if (rentalRepository.existsById(id))
            rentalRepository.deleteById(id);
        else
            throw new ResourceNotFoundException(String.format("Rental with id %d not found", id));
    }

    @Override
    public boolean isCarFreeInFuture(Long id) {
        return rentalRepository.existsByCarIdAndDateToAfter(id, LocalDate.now());
    }

    @Override
    public boolean isCarFree(Long id, LocalDate start, LocalDate end) {
        return !rentalRepository.existsByCarIdAndDateFromBetweenOrDateToBetween(id, start, end, start, end);
    }
}
