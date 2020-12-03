package pl.microservices.rentalservice.web;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import pl.microservices.rentalservice.exception.UnauthorizedRequestException;
import pl.microservices.rentalservice.model.Rental;
import pl.microservices.rentalservice.service.RentalService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {
    private RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping(params = { "page", "size" })
    @RolesAllowed({ "moderator", "admin" })
    public List<Rental> findAllPaged(@RequestParam int page, @RequestParam int size) {
        return rentalService.findAll(page, size)
                .getContent();
    }

    @GetMapping("/users/{requestedUsername}")
    public Iterable<Rental> findByUsername(@PathVariable String requestedUsername) {
        return rentalService.findByUsername(requestedUsername);
    }

    @GetMapping("/{id}")
    @RolesAllowed({ "user", "moderator", "admin" })
    public Rental findById(@PathVariable Long id, Authentication authentication) {
        Rental rental = rentalService.findById(id);

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        String username = ((Principal) authentication.getPrincipal()).getName();

        if (isRegularUser(roles))
            ownershipCheck(rental.getUsername(), username);

        return rental;
    }

    private boolean isRegularUser(Collection<? extends GrantedAuthority> roles) {
        return roles.stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(role -> role.equals("moderator") || role.equals("admin"));
    }

    private void ownershipCheck(String requestedUsername, String username) {
        if (requestedUsername.equals(username))
            throw new UnauthorizedRequestException("Action only permitted for resource owners");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed({ "moderator", "admin" })
    public Long save(@Valid @RequestBody Rental rental) {
        rental.setId(null);

        return rentalService.save(rental)
                .getId();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ "moderator", "admin" })
    public void put(@Valid @RequestBody Rental rental) {
        if (Objects.isNull(rental.getId()))
            throw new IllegalArgumentException("Id need not be null while updating");

        rentalService.save(rental);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed({ "moderator", "admin" })
    public void delete(@PathVariable Long id) {
        rentalService.deleteById(id);
    }

    @GetMapping("/cars/{id}/isFree")
    public boolean isCarFreeInFuture(@PathVariable Long id) {
        return rentalService.isCarFreeInFuture(id);
    }

    @GetMapping(path = "/cars/{id}/isFree", params = { "start", "end" })
    public boolean isCarFreeBetween(@PathVariable Long id,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return rentalService.isCarFree(id, start, end);
    }
}
