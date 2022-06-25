package disaster.config;

import disaster.module.auth.oauth.*;
import disaster.module.auth.oauth.handler.OauthFailureHandler;
import disaster.module.auth.oauth.handler.OauthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-oauth2.properties")
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class SecurityConfig {
    private final OAuth2ClientProperties clientProperties;

    @Autowired
    public SecurityConfig(OAuth2ClientProperties oAuth2ClientProperties) {
        this.clientProperties = oAuth2ClientProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable();
        configureOauth(http);
        return http.build();
    }

    public void configureOauth(HttpSecurity http) throws Exception {
        var registrationRepository = clientRegistrationRepository(clientProperties);
        var requestResolver = oauthRequestResolver(registrationRepository);
        var clientRepository = authorizedClientRepository();

        http.oauth2Login()
            .authorizationEndpoint()
            .authorizationRequestResolver(requestResolver)
            .authorizationRequestRepository(new CustomOauthRequestRepository())
            .and()
            .clientRegistrationRepository(registrationRepository)
            .authorizedClientRepository(clientRepository)
            .loginProcessingUrl("/auth/oauth/login/*")
            .successHandler(new OauthSuccessHandler())
            .failureHandler(new OauthFailureHandler())
            .userInfoEndpoint()
            .userService(new CustomOauthUserService());
    }

    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }

    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oauthClientProperties) {
        var registrationsMap = OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oauthClientProperties);
        List<ClientRegistration> registrations = new ArrayList<>(registrationsMap.values());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    public OAuth2AuthorizationRequestResolver oauthRequestResolver(ClientRegistrationRepository clientRepository) {
        String resolverUrl = "/auth/oauth/init";
        return new CustomOauthRequestResolver(clientRepository, resolverUrl);
    }
}
