package pl.microservices.userservice.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRequest {
    @Size(min = 5, max = 20)
    @NotNull
    private String username;
    @Size(min = 5, max = 20)
    @NotNull
    private String password;
    @Size(min = 2, max = 25)
    @NotNull
    private String firstName;
    @Size(min = 2, max = 25)
    @NotNull
    private String lastName;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
    @Size(min = 5, max = 25)
    @NotNull
    private String email;
}
