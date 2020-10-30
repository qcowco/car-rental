package pl.microservices.userservice;

import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(KeycloakConfig.class)
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakConfig {
    private String authServerUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                )
                .build();
    }

    @Bean
    public org.keycloak.authorization.client.Configuration configuration() {
        return new org.keycloak.authorization.client.Configuration(
                authServerUrl,
                realm,
                clientId,
                Map.of("secret", clientSecret),
                null);
    }

}
