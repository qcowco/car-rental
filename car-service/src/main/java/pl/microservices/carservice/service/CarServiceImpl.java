package pl.microservices.carservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.microservices.carservice.dao.CarRepository;
import pl.microservices.carservice.exception.ResourceNotFound;
import pl.microservices.carservice.model.Car;

@Service
public class CarServiceImpl implements CarService {
    private CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Iterable<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Page<Car> findAll(int page, int size) {
        return carRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Car with id %d not found", id)));
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void deleteById(Long id) {
        if (carRepository.existsById(id))
            carRepository.deleteById(id);
        else
            throw new ResourceNotFound(String.format("Car with id %d not found", id));
    }

}
