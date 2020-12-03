package pl.microservices.rentalrequestservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.microservices.rentalrequestservice.repository.RentalRequestRepository;
import pl.microservices.rentalrequestservice.exception.ResourceNotFoundException;
import pl.microservices.rentalrequestservice.model.RentalRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalRequestServiceImplTest {
    private final int PAGE = 1;
    private final int PAGE_SIZE = 10;

    private final String USERNAME = "USERNAME";
    private final long RENTAL_REQUEST_ID = 1L;

    @InjectMocks
    private RentalRequestServiceImpl rentalRequestService;

    @Mock
    private RentalRequestRepository rentalRequestRepository;

    private RentalRequest rentalRequest = new RentalRequest();

    @DisplayName("Given all rental requests are being requested")
    @Nested
    class GetAll {
        @Mock
        private Page<RentalRequest> page;

        @DisplayName("Then returns a page of rental requests")
        @Test
        public void shouldReturnPage() {
            when(rentalRequestRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE)))
                    .thenReturn(page);

            Page<RentalRequest> actualPage = rentalRequestService.findAll(PAGE, PAGE_SIZE);

            assertEquals(page, actualPage);
        }
    }

    @DisplayName("Given rental requests by username are being requested")
    @Nested
    class GetByUsername {
        private List<RentalRequest> rentalRequests;

        @DisplayName("Then returns a collection of rental requests")
        @Test
        public void shouldReturnRentalRequests() {
            rentalRequests = List.of(new RentalRequest(), new RentalRequest());

            when(rentalRequestRepository.findByUsername(USERNAME))
                    .thenReturn(rentalRequests);

            Iterable<RentalRequest> actualRentalRequests = rentalRequestService.findByUsername(USERNAME);

            assertIterableEquals(rentalRequests, actualRentalRequests);
        }
    }

    @DisplayName("Given a rental request is being requested")
    @Nested
    class GetOne {

        @DisplayName("Then returns a rental request with specified id")
        @Test
        public void shouldReturnRentalRequest() {
            when(rentalRequestRepository.findById(RENTAL_REQUEST_ID))
                    .thenReturn(Optional.of(rentalRequest));

            RentalRequest actualRentalRequest = rentalRequestService.findById(RENTAL_REQUEST_ID);

            assertEquals(rentalRequest, actualRentalRequest);
        }

        @DisplayName("When that rental request is not found")
        @Nested
        class NotFound {

            @DisplayName("Then throws ResourceNotFoundException")
            @Test
            public void shouldThrow_ResourceNotFoundException() {
                when(rentalRequestRepository.findById(RENTAL_REQUEST_ID))
                        .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> rentalRequestService.findById(RENTAL_REQUEST_ID));
            }
        }
    }

    @DisplayName("Given a rental request is being saved")
    @Nested
    class SaveRequest {

        @DisplayName("Then returns a persisted instance")
        @Test
        public void shouldReturnRentalRequest() {
            RentalRequest persistedRentalRequest = new RentalRequest();
            persistedRentalRequest.setId(1L);

            when(rentalRequestRepository.save(rentalRequest))
                    .thenReturn(persistedRentalRequest);

            RentalRequest actualRentalRequest = rentalRequestService.save(rentalRequest);

            assertEquals(persistedRentalRequest, actualRentalRequest);
        }
    }

    @DisplayName("Given a rental request is being deleted")
    @Nested
    class DeleteRequest {

        @DisplayName("When not found")
        @Nested
        class NotFound {

            @DisplayName("Then throws ResourceNotFoundException")
            @Test
            public void shouldThrow_ResourceNotFoundException() {
                when(rentalRequestRepository.existsById(RENTAL_REQUEST_ID))
                        .thenReturn(false);

                assertThrows(ResourceNotFoundException.class, () -> rentalRequestService.deleteById(RENTAL_REQUEST_ID));
            }
        }
    }
}
