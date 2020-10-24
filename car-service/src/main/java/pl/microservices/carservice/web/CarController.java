package pl.microservices.carservice.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pl.microservices.carservice.dao.CarRepository;
import pl.microservices.carservice.model.Car;

import java.util.List;
import java.util.Optional;

@RestController("/api/v1/cars")
public class CarController {
    private CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    public Iterable<Car> findAll() {
        return carRepository.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<Car> findAllPaged(@RequestParam int page, @RequestParam int size) {
        Page<Car> all = carRepository.findAll(PageRequest.of(page, size));

        return all.getContent();
    }

    @GetMapping("/{id}")
    public Car findById(@PathVariable Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);

        return optionalCar.orElseThrow();
    }

    @PostMapping
    public Long save(@RequestBody Car car) {
        Car save = carRepository.save(car);

        return save.getId();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        carRepository.deleteById(id);
    }

    @PutMapping
    public void put(@RequestBody Car car) {
        carRepository.save(car);
    }
}
