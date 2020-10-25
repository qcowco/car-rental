package pl.microservices.rentalservice.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.microservices.rentalservice.model.Rental;

@Repository
public interface RentalRepository extends PagingAndSortingRepository<Rental, Long> {
}
