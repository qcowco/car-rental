package pl.microservices.carservice.service;

import org.springframework.data.domain.Page;
import pl.microservices.carservice.model.Car;

public interface CarService {
    Iterable<Car> findAll();

    Page<Car> findAll(int page, int size);

    Car findById(Long id);

    Car save(Car car);

    void deleteById(Long id);
}
