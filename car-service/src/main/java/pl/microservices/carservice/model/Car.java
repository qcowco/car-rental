package pl.microservices.carservice.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.javamoney.moneta.Money;

import javax.persistence.*;
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

    private int productionYear;
    private String make;
    private String model;
    private String driveType;
    private String fuelType;
    private String transmission;

    @CreationTimestamp
    private LocalDateTime registeredAt;
    private Address address;

    private Money pricePerDay;
}
