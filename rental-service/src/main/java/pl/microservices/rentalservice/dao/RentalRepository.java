package pl.microservices.rentalservice.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.microservices.rentalservice.model.Rental;

import java.time.LocalDate;

@Repository
public interface RentalRepository extends PagingAndSortingRepository<Rental, Long> {
    boolean existsByCarIdAndDateFromBetweenOrDateToBetween(
            Long carId, LocalDate dateFrom, LocalDate dateFrom2, LocalDate dateTo, LocalDate dateTo2
    );
}
