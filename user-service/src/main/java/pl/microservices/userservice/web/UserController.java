package pl.microservices.userservice.web;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.microservices.userservice.model.LoginRequest;
import pl.microservices.userservice.model.UserRequest;
import pl.microservices.userservice.service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {
    private UserService userService;
    private Keycloak keycloak;
    private AuthzClient authzClient;

    public UserController(UserService userService, Keycloak keycloak, AuthzClient authzClient) {
        this.userService = userService;
        this.keycloak = keycloak;
        this.authzClient = authzClient;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequest userRequest) {
        RealmResource realmResource = keycloak.realm("SpringBootKeycloak");
        UsersResource users = realmResource.users();

        UserRepresentation userRepresentation = userService.getUserRepresentation(userRequest);

        users.create(userRepresentation);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest) {
        AccessTokenResponse accessTokenResponse = authzClient.obtainAccessToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        return accessTokenResponse.getToken();
    }

}
