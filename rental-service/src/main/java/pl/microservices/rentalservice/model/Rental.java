package pl.microservices.rentalservice.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(
            name = "sequence_generator",
            sequenceName = "rental_generator"
    )
    private Long id;
    @NotNull
    private Long carId;
    @NotNull
    private String username;
    @FutureOrPresent
    private LocalDate dateFrom;
    @Future
    private LocalDate dateTo;
    private boolean paidFor;
}
