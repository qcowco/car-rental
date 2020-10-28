package pl.microservices.userservice.service;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import pl.microservices.userservice.model.UserRequest;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserRepresentation getUserRepresentation(UserRequest userRequest) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userRequest.getUsername());
        userRepresentation.setFirstName(userRequest.getFirstName());
        userRepresentation.setLastName(userRequest.getLastName());
        userRepresentation.setEmail(userRequest.getEmail());
        userRepresentation.setCredentials(List.of(getCredentialRepresentation(userRequest)));
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }

    private CredentialRepresentation getCredentialRepresentation(UserRequest userRequest) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();

        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userRequest.getPassword());
        credentialRepresentation.setTemporary(false);

        return credentialRepresentation;
    }
}
