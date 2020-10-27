package pl.microservices.rentalrequestservice.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.microservices.rentalrequestservice.model.RentalRequest;

@Repository
public interface RentalRequestRepository extends PagingAndSortingRepository<RentalRequest, Long> {
    Iterable<RentalRequest> findByUsername(String username);
}
