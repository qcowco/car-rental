package pl.microservices.rentalrequestservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignTokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof KeycloakAuthenticationToken) {
            KeycloakSecurityContext context = (KeycloakSecurityContext) authentication.getCredentials();

            String jwtToken = context.getTokenString();

            template.header("Authorization", String.format("Bearer %s", jwtToken));
        }

    }
}
