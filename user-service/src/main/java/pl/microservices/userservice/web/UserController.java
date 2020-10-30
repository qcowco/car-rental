package pl.microservices.userservice.web;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
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
    private Configuration configuration;
    private AuthzClient authzClient;

    public UserController(UserService userService, Keycloak keycloak, Configuration configuration) {
        this.userService = userService;
        this.keycloak = keycloak;
        this.configuration = configuration;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequest userRequest) {
        RealmResource realmResource = keycloak.realm("SpringBootKeycloak");
        UsersResource users = realmResource.users();

        accountIsUnique(userRequest, users);

        UserRepresentation userRepresentation = userService.getUserRepresentation(userRequest);

        users.create(userRepresentation);
    }

    private void accountIsUnique(@RequestBody @Valid UserRequest userRequest, UsersResource users) {
        usernameIsUnique(users, userRequest.getUsername());

        emailIsUnique(users, userRequest.getEmail());
    }

    private void emailIsUnique(UsersResource users, String email) {
        boolean present = users.list()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
        if (present)
            throw new IllegalArgumentException("Email is already in use");
    }

    private void usernameIsUnique(UsersResource users, String username) {
        if (users.search(username).size() > 0)
            throw new IllegalArgumentException("Username is already in use");
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest) {
        AccessTokenResponse accessTokenResponse = getAuthzClient().obtainAccessToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        return accessTokenResponse.getToken();
    }

    private AuthzClient getAuthzClient() {
        if (authzClient == null)
            authzClient = AuthzClient.create(configuration);

        return authzClient;
    }
}
