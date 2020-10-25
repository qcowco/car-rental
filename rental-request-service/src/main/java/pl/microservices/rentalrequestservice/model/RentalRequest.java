package pl.microservices.rentalrequestservice.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class RentalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(
            name = "sequence_generator",
            sequenceName = "rental_request_generator"
    )
    private Long id;
    @NotNull
    private Long carId;
    @NotNull
    private Long userId;
    @FutureOrPresent
    private LocalDateTime dateFrom;
    @Future
    private LocalDateTime dateTo;
}
