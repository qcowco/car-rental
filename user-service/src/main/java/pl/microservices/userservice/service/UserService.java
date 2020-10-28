package pl.microservices.userservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import pl.microservices.userservice.model.UserRequest;

public interface UserService {
    UserRepresentation getUserRepresentation(UserRequest userRequest);
}
