package pl.microservices.rentalrequestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RentalRequestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalRequestServiceApplication.class, args);
    }

}
