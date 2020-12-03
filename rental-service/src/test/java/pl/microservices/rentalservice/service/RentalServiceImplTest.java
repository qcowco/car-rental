package pl.microservices.rentalservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.microservices.rentalservice.repository.RentalRepository;
import pl.microservices.rentalservice.exception.ResourceNotFoundException;
import pl.microservices.rentalservice.model.Rental;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {
    private final int PAGE = 0;
    private final int PAGE_SIZE = 10;
    private final String USERNAME = "USERNAME";
    private final long RENTAL_ID = 1;
    private final long CAR_ID = 1;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private Iterable<Rental> rentals;

    @DisplayName("Given all rentals are requested")
    @Nested
    class GetAll {

        @DisplayName("When paging is defined")
        @Nested
        class Paged {
            @Mock
            private Page<Rental> page;

            @DisplayName("Then return a page of rentals")
            @Test
            public void shouldReturnPageOfRentals() {
                when(rentalRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE)))
                        .thenReturn(page);

                Page<Rental> fetchedRentalPage = rentalService.findAll(PAGE, PAGE_SIZE);

                assertEquals(page, fetchedRentalPage);
            }
        }
    }

    @DisplayName("Given rentals are requested by username")
    @Nested
    class GetUsersRentals {

        @DisplayName("Then returns a collection of rentals")
        @Test
        public void shouldReturnRentals() {
            when(rentalRepository.findByUsername(USERNAME))
                    .thenReturn(rentals);

            Iterable<Rental> fetchedRentals = rentalService.findByUsername(USERNAME);

            assertEquals(rentals, fetchedRentals);
        }
    }

    @DisplayName("Given a rental is requested")
    @Nested
    class GetOne {

        @DisplayName("Then returns a rental")
        @Test
        public void shouldReturnRental() {
            Rental rental = new Rental();

            when(rentalRepository.findById(RENTAL_ID))
                    .thenReturn(Optional.of(rental));

            Rental actualRental = rentalService.findById(RENTAL_ID);

            assertEquals(rental, actualRental);
        }

        @DisplayName("When rental with that id is not found")
        @Nested
        class NotFound {

            @DisplayName("Then throws ResourceNotFoundException")
            @Test
            public void shouldThrow_ResourceNotFoundException() {
                when(rentalRepository.findById(RENTAL_ID))
                        .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> rentalService.findById(RENTAL_ID));
            }
        }
    }

    @DisplayName("Given a rental is being saved")
    @Nested
    class SaveRental {

        @DisplayName("Then returns persisted rental")
        @Test
        public void shouldReturnRental() {
            Rental rental = new Rental();

            when(rentalRepository.save(rental))
                    .thenReturn(rental);

            Rental actualRental = rentalService.save(rental);

            assertEquals(rental, actualRental);
        }
    }

    @DisplayName("Give a rental is being deleted")
    @Nested
    class DeleteRental {

        @DisplayName("When the rental isn't found")
        @Nested
        class NotFound {

            @DisplayName("Then throws ResourceNotFoundException")
            @Test
            public void shouldThrow_ResourceNotFoundException() {
                when(rentalRepository.existsById(RENTAL_ID))
                        .thenReturn(false);

                assertThrows(ResourceNotFoundException.class, () -> rentalService.deleteById(RENTAL_ID));
            }
        }
    }

    @DisplayName("Given a car is being checked for future rentals")
    @Nested
    class RentalAvailability {

        @DisplayName("Then returns whether car is completely free in the future")
        @Test
        public void shouldReturnWhetherCarIsFree() {
            when(rentalRepository.existsByCarIdAndDateToAfter(eq(CAR_ID), any(LocalDate.class)))
                    .thenReturn(false);

            boolean response = rentalService.isCarFreeInFuture(CAR_ID);

            assertTrue(response);
        }
    }

    @DisplayName("Given a car is being checked for rentals within a timeframe")
    @Nested
    class RentalTimeframeAvailability {

        @DisplayName("Then returns whether car is free between dates")
        @Test
        public void shouldReturnWhetherCarIsFree() {
            // TODO: 25.11.2020 refactor original repository method
        }
    }
}
