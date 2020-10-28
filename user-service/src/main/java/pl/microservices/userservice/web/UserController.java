package pl.microservices.userservice.web;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.microservices.userservice.model.UserRequest;
import pl.microservices.userservice.service.UserService;

@RestController
public class UserController {
    private UserService userService;
    private Keycloak keycloak;

    public UserController(UserService userService, Keycloak keycloak) {
        this.userService = userService;
        this.keycloak = keycloak;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserRequest userRequest) {
        RealmResource realmResource = keycloak.realm("SpringBootKeycloak");
        UsersResource users = realmResource.users();

        UserRepresentation userRepresentation = userService.getUserRepresentation(userRequest);

        users.create(userRepresentation);
    }

}
