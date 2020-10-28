package pl.microservices.userservice.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @Size(min = 5, max = 20)
    @NotNull
    private String username;
    @Size(min = 5, max = 20)
    @NotNull
    private String password;
}
