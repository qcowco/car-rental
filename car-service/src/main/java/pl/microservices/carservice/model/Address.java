package pl.microservices.carservice.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Data
@Embeddable
public class Address {
    private String city;
    private String streetAddress;
    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String zipCode;
}
