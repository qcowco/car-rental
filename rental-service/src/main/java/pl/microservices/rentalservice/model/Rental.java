package pl.microservices.rentalservice.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long carId;
    private Long userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private boolean paidFor;
}
