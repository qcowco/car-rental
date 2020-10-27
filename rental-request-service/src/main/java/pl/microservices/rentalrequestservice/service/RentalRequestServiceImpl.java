package pl.microservices.rentalrequestservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.microservices.rentalrequestservice.dao.RentalRequestRepository;
import pl.microservices.rentalrequestservice.exception.ResourceNotFoundException;
import pl.microservices.rentalrequestservice.model.RentalRequest;

@Service
public class RentalRequestServiceImpl implements RentalRequestService {
    private RentalRequestRepository rentalRequestRepository;

    public RentalRequestServiceImpl(RentalRequestRepository rentalRequestRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @Override
    public Iterable<RentalRequest> findByUsername(String username) {
        return rentalRequestRepository.findByUsername(username);
    }

    @Override
    public Page<RentalRequest> findAll(int page, int size) {
        return rentalRequestRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public RentalRequest findById(Long id) {
        return rentalRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Request with id %d not found", id)));
    }

    @Override
    public RentalRequest save(RentalRequest rentalRequest) {
        return rentalRequestRepository.save(rentalRequest);
    }

    @Override
    public void deleteById(Long id) {
        if (rentalRequestRepository.existsById(id))
            rentalRequestRepository.deleteById(id);
        else
            throw new ResourceNotFoundException(String.format("Request with id %d not found", id));
    }
}
