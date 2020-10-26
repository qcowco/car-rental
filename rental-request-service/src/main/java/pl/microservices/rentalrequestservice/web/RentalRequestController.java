package pl.microservices.rentalrequestservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalrequestservice.model.RentalRequest;
import pl.microservices.rentalrequestservice.service.RentalRequestService;
import pl.microservices.rentalrequestservice.web.dto.RequestEvaluation;
import pl.microservices.rentalrequestservice.web.webclient.CarClient;
import pl.microservices.rentalrequestservice.web.webclient.RentalClient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/v1/rental-requests")
public class RentalRequestController {
    private RentalRequestService rentalRequestService;
    private CarClient carClient;
    private RentalClient rentalClient;

    public RentalRequestController(RentalRequestService rentalRequestService, CarClient carClient, RentalClient rentalClient) {
        this.rentalRequestService = rentalRequestService;
        this.carClient = carClient;
        this.rentalClient = rentalClient;
    }

    @GetMapping
    public Iterable<RentalRequest> findAll() {
        return rentalRequestService.findAll();
    }

    @GetMapping(params = { "page", "size" })
    public List<RentalRequest> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalRequestService.findAll(page, size)
                .getContent();
    }

    @GetMapping("/{id}")
    public RentalRequest findById(@PathVariable Long id) {
        return rentalRequestService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@Valid @RequestBody RentalRequest rentalRequest) {
        if (Objects.nonNull(rentalRequest.getId()))
            throw new IllegalArgumentException("Id needs to be empty");

        isCarRentable(rentalRequest);

        return rentalRequestService.save(rentalRequest)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@Valid @RequestBody RentalRequest rentalRequest) {
        if (Objects.isNull(rentalRequest.getId()))
            throw new IllegalArgumentException("Id need not be null while updating");

        isCarRentable(rentalRequest);

        rentalRequestService.save(rentalRequest);
    }

    private void isCarRentable(RentalRequest rentalRequest) {
        carExists(rentalRequest.getCarId());

        isFree(rentalRequest.getCarId(), rentalRequest.getDateFrom(), rentalRequest.getDateTo());
    }

    private void isFree(Long carId, LocalDate dateFrom, LocalDate dateTo) {
        if (!rentalClient.isCarFree(carId, dateFrom, dateTo))
            throw new IllegalArgumentException(String.format(
                    "The selected car isn't free between %s and %s",
                    dateFrom.toString(),
                    dateTo.toString())
            );
    }

    private void carExists(@NotNull Long carId) {
        if (Objects.isNull(carClient.findById(carId)))
            throw new IllegalArgumentException("The selected car doesn't exist");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalRequestService.deleteById(id);
    }

}
