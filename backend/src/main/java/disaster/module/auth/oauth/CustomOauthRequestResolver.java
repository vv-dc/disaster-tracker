package disaster.module.auth.oauth;

import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomOauthRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

    private final ServerOAuth2AuthorizationRequestResolver internalResolver;

    public CustomOauthRequestResolver(ReactiveClientRegistrationRepository repository, String resolveUrl) {
        this.internalResolver = new DefaultServerOAuth2AuthorizationRequestResolver(
            repository,
            ServerWebExchangeMatchers.pathMatchers(resolveUrl)
        );
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        return internalResolver.resolve(exchange)
            .map((context) -> decorateContext(exchange, context));
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String registrationId) {
        return internalResolver.resolve(exchange, registrationId)
            .map((context) -> decorateContext(exchange, context));
    }

    private OAuth2AuthorizationRequest decorateContext(ServerWebExchange exchange, OAuth2AuthorizationRequest context) {
        var additionalParams = OauthRequestStateUtils.buildAdditionalParamsFromRequest(exchange, context);
        return OAuth2AuthorizationRequest
            .from(context)
            .additionalParameters(additionalParams)
            .build();
    }
}
