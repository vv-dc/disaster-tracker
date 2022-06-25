package disaster.module.auth.oauth;

import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionOAuth2ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomOauthRequestRepository implements ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> internalRepository;

    public CustomOauthRequestRepository() {
        this.internalRepository = new WebSessionOAuth2ServerAuthorizationRequestRepository();
    }

    public Mono<OAuth2AuthorizationRequest> loadAuthorizationRequest(ServerWebExchange request) {
        return internalRepository.loadAuthorizationRequest(request);
    }

    public Mono<Void> saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, ServerWebExchange exchange) {
        return internalRepository.saveAuthorizationRequest(authorizationRequest, exchange);
    }

    public Mono<OAuth2AuthorizationRequest> removeAuthorizationRequest(ServerWebExchange exchange) {
        return internalRepository.removeAuthorizationRequest(exchange)
            .map((context) -> {
                OauthRequestStateUtils.setOauthStateToRequest(exchange, context);
                return context;
            });
    }
}
