package pl.microservices.carservice.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(
            name = "sequence_generator",
            sequenceName = "car_sequence"
    )
    private Long id;

    @Range(min = 1900, max = 2100)
    private Integer productionYear;
    private String make;
    private String model;
    private String driveType;
    private String fuelType;
    private String transmission;

    @CreationTimestamp
    private LocalDateTime registeredAt;
    @Valid
    private Address address;

    @Positive
    private BigDecimal pricePerDay;

}
