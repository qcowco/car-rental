package pl.microservices.carservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.microservices.carservice.repository.CarRepository;
import pl.microservices.carservice.exception.ResourceNotFoundException;
import pl.microservices.carservice.model.Car;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    private final long CAR_ID = 1;
    private final int PAGE = 0;
    private final int PAGE_SIZE = 10;

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private Iterable<Car> cars;

    @Mock
    private Page<Car> carPage;

    @Mock
    private CarRepository carRepository;

    @DisplayName("Given all cars are being requested")
    @Nested
    class GetAllCars {

        @DisplayName("Then returns a collection of all cars")
        @Test
        public void shouldReturnAllCars() {
            when(carRepository.findAll())
                    .thenReturn(cars);

            Iterable<Car> fetchedCars = carService.findAll();

            assertEquals(cars, fetchedCars);
        }

        @DisplayName("When paging is defined")
        @Nested
        class GetAllPaged {

            @DisplayName("Then returns a page of cars")
            @Test
            public void shouldReturnPageOfCars() {
                when(carRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE)))
                        .thenReturn(carPage);

                Page<Car> fetchedCarPage = carService.findAll(PAGE, PAGE_SIZE);

                assertEquals(carPage, fetchedCarPage);
            }
        }
    }

    @DisplayName("Given a specific car is being requested")
    @Nested
    class GetCar {

        @DisplayName("When the car exists")
        @Nested
        class CarExists {

            @DisplayName("Then returns that car")
            @Test
            public void shouldReturnCar() {
                Car car = new Car();

                when(carRepository.findById(CAR_ID))
                        .thenReturn(Optional.of(car));

                Car fetchedCar = carService.findById(CAR_ID);

                assertEquals(car, fetchedCar);
            }
        }

        @DisplayName("When the car isn't found")
        @Nested
        class CarNotFound {

            @DisplayName("Then throws ResourceNotFoundException")
            @Test
            public void shouldThrow_ResourceNotFoundException() {
                when(carRepository.findById(CAR_ID))
                        .thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> carService.findById(CAR_ID));
            }
        }
    }

    @DisplayName("Given a car is being saved")
    @Nested
    class SaveCar {

        @DisplayName("Then returns persisted car")
        @Test
        public void shouldReturnPersistedCar() {
            Car car = new Car();

            when(carRepository.save(car))
                    .thenReturn(car);

            Car persistedCar = carService.save(car);

            assertEquals(car, persistedCar);
        }
    }

    @DisplayName("Given a car is being deleted")
    @Nested
    class DeleteCar {

        @DisplayName("When car isn't found")
        @Nested
        class CarNotFound {

            @DisplayName("Then throws ResourceNotFoundException")
            @Test
            public void shouldThrow_ResourceNotFoundException() {
                when(carRepository.existsById(CAR_ID))
                        .thenReturn(false);

                assertThrows(ResourceNotFoundException.class, () -> carService.deleteById(CAR_ID));
            }
        }
    }
}
