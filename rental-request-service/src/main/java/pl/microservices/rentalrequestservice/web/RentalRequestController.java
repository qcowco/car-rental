package pl.microservices.rentalrequestservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalrequestservice.exception.UnauthorizedRequestException;
import pl.microservices.rentalrequestservice.model.RentalRequest;
import pl.microservices.rentalrequestservice.service.RentalRequestService;
import pl.microservices.rentalrequestservice.web.dto.RequestEvaluation;
import pl.microservices.rentalrequestservice.web.webclient.client.CarClient;
import pl.microservices.rentalrequestservice.web.webclient.client.RentalClient;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
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

    @GetMapping(params = { "page", "size" })
    @RolesAllowed({ "moderator", "admin" })
    public List<RentalRequest> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalRequestService.findAll(page, size)
                .getContent();
    }

    @GetMapping("/users/{requestedUsername}")
    @RolesAllowed({ "user", "moderator", "admin" })
    @PreAuthorize("#username == authentication.principal.username || #roles == authentication.authorities")
    public Iterable<RentalRequest> findByUsername(@PathVariable String requestedUsername, String username, Collection<GrantedAuthority> roles) {

        if (isRegularUser(roles))
            ownershipCheck(requestedUsername, username);

        return rentalRequestService.findByUsername(requestedUsername);
    }

    @GetMapping("/{id}")
    @RolesAllowed({ "user", "moderator", "admin" })
    @PreAuthorize("#username == authentication.principal.username || #roles == authentication.authorities")
    public RentalRequest findById(@PathVariable Long id, String username, Collection<GrantedAuthority> roles) {
        RentalRequest rentalRequest = rentalRequestService.findById(id);

        if (isRegularUser(roles))
            ownershipCheck(rentalRequest.getUsername(), username);

        return rentalRequest;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({ "user", "moderator", "admin" })
    @PreAuthorize("#username == authentication.principal.username")
    public Long save(@Valid @RequestBody RentalRequest rentalRequest, String username) {
        rentalRequest.setId(null);
        rentalRequest.setUsername(username);

        isCarRentable(rentalRequest);

        return rentalRequestService.save(rentalRequest)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ "user", "moderator", "admin" })
    @PreAuthorize("#username == authentication.principal.username || #roles == authentication.authorities")
    public void put(@Valid @RequestBody RentalRequest rentalRequest, String username, Collection<GrantedAuthority> roles) {
        if (Objects.isNull(rentalRequest.getId()))
            throw new IllegalArgumentException("Id need not be null while updating");

        if (isRegularUser(roles))
            ownershipCheck(rentalRequest.getUsername(), username);

        isCarRentable(rentalRequest);

        rentalRequestService.save(rentalRequest);
    }

    private boolean isRegularUser(Collection<GrantedAuthority> roles) {
        return roles.stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(role -> role.equals("moderator") || role.equals("admin"));
    }

    private void ownershipCheck(String requestedUsername, String username) {
        if (!requestedUsername.equals(username))
            throw new UnauthorizedRequestException("Action only permitted for resource owners");
    }

    private void isCarRentable(RentalRequest rentalRequest) {
        carExists(rentalRequest.getCarId());

        isFree(rentalRequest.getCarId(), rentalRequest.getDateFrom(), rentalRequest.getDateTo());
    }

    private void carExists(@NotNull Long carId) {
        if (Objects.isNull(carClient.findById(carId)))
            throw new IllegalArgumentException("The selected car doesn't exist");
    }

    private void isFree(Long carId, LocalDate dateFrom, LocalDate dateTo) {
        if (!rentalClient.isCarFree(carId, dateFrom, dateTo))
            throw new IllegalArgumentException(String.format(
                    "The selected car isn't free between %s and %s",
                    dateFrom.toString(),
                    dateTo.toString())
            );
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ "moderator", "admin" })
    public void evaluateRequest(@PathVariable Long id, @RequestBody RequestEvaluation requestEvaluation) {
        if (requestEvaluation.isApproved()) {
            RentalRequest rentalRequest = rentalRequestService.findById(id);

            isCarRentable(rentalRequest);

            rentalClient.save(rentalRequest);
        }

        rentalRequestService.deleteById(id);
    }

}
