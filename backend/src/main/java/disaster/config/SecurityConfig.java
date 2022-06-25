package disaster.config;

import disaster.module.auth.oauth.CustomOauthRequestRepository;
import disaster.module.auth.oauth.CustomOauthRequestResolver;
import disaster.module.auth.oauth.CustomOauthUserService;
import disaster.module.auth.oauth.handler.OauthFailureHandler;
import disaster.module.auth.oauth.handler.OauthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@PropertySource("classpath:application-oauth2.properties")
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class SecurityConfig {

    private final OAuth2ClientProperties clientProperties;

    @Autowired
    public SecurityConfig(OAuth2ClientProperties oAuth2ClientProperties) {
        this.clientProperties = oAuth2ClientProperties;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http.cors().disable();
        configureOauth(http);
        return http.build();
    }

    public void configureOauth(ServerHttpSecurity http) {
        var registrationRepository = clientRegistrationRepository(clientProperties);
        var requestResolver = oauthRequestResolver(registrationRepository);

        http.oauth2Login()
            .authorizationRequestResolver(requestResolver)
            .authorizationRequestRepository(new CustomOauthRequestRepository())
            .clientRegistrationRepository(registrationRepository)
            .authenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/oauth/login/{registrationId}"))
            .authenticationSuccessHandler(new OauthSuccessHandler("/error"))
            .authenticationFailureHandler(new OauthFailureHandler("/error"));
        // NOTE: useService can be only provided via Bean
    }

    private ReactiveClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oauthClientProperties) {
        var registrationsMap = OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(oauthClientProperties);
        List<ClientRegistration> registrations = new ArrayList<>(registrationsMap.values());
        return new InMemoryReactiveClientRegistrationRepository(registrations);
    }

    private ServerOAuth2AuthorizationRequestResolver oauthRequestResolver(ReactiveClientRegistrationRepository clientRepository) {
        String resolverUrl = "/auth/oauth/init/{registrationId}";
        return new CustomOauthRequestResolver(clientRepository, resolverUrl);
    }

    @Bean
    ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
        return new CustomOauthUserService();
    }
}
