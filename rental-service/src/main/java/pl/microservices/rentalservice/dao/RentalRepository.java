package pl.microservices.rentalservice.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.microservices.rentalservice.model.Rental;

import java.time.LocalDate;

@Repository
public interface RentalRepository extends PagingAndSortingRepository<Rental, Long> {
    Iterable<Rental> findByUsername(String username);

    boolean existsByCarIdAndDateToAfter(Long carId, LocalDate now);
    boolean existsByCarIdAndDateFromBetweenOrCarIdAndDateToBetween(
            Long carId, LocalDate dateFrom, LocalDate dateFrom2, Long carId2, LocalDate dateTo, LocalDate dateTo2
    );
}
