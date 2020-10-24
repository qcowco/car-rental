package pl.microservices.carservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.carservice.model.Car;
import pl.microservices.carservice.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {
    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public Iterable<Car> findAll() {
        return carService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<Car> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return carService.findAll(page, size)
                .getContent();
    }

    @GetMapping("/{id}")
    public Car findById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@RequestBody Car car) {
        return carService.save(car)
                .getId();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        carService.deleteById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestBody Car car) {
        carService.save(car);
    }
}
