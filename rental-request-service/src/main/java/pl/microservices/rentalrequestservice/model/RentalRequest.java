package pl.microservices.rentalrequestservice.model;

import lombok.Data;

import javax.persistence.*;
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
    private Long carId;
    private Long userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
}
