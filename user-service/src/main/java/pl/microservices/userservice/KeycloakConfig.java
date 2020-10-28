package pl.microservices.userservice;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KeycloakConfig {
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth")
                .realm("SpringBootKeycloak")
                .clientId("auth-app")
                .clientSecret("040afe3d-9d18-4001-965a-f3fbc317b20b")
                .username("usermngr")
                .password("mngr")
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                )
                .build();
    }

    @Bean
    public AuthzClient authzClient() {
        org.keycloak.authorization.client.Configuration configuration = new org.keycloak.authorization.client.Configuration(
                "http://localhost:8080/auth",
                "SpringBootKeycloak",
                "auth-app",
                Map.of("secret", "040afe3d-9d18-4001-965a-f3fbc317b20b"),
                null);

        return AuthzClient.create(configuration);
    }
}
