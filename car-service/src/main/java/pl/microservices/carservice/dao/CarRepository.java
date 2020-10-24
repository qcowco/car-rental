package pl.microservices.carservice.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.microservices.carservice.model.Car;

@Repository
public interface CarRepository extends PagingAndSortingRepository<Car, Long> {
}
