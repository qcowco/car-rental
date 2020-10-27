package pl.microservices.rentalrequestservice.service;

import org.springframework.data.domain.Page;
import pl.microservices.rentalrequestservice.model.RentalRequest;

public interface RentalRequestService {
    Iterable<RentalRequest> findByUsername(String username);

    Page<RentalRequest> findAll(int page, int size);

    RentalRequest findById(Long id);

    RentalRequest save(RentalRequest rentalRequest);

    void deleteById(Long id);
}
