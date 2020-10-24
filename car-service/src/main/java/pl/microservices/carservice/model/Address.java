package pl.microservices.carservice.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {
    private String city;
    private String streetAddress;
    private String zipCode;
}
