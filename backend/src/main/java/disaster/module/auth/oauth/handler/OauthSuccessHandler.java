package disaster.module.auth.oauth.handler;

import disaster.module.auth.oauth.CustomOauthUser;
import disaster.module.auth.oauth.state.OauthRequestState;
import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class OauthSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    private final String defaultRedirectUrl;

    public OauthSuccessHandler(String defaultRedirectUrl) {
        super(defaultRedirectUrl);
        this.defaultRedirectUrl = defaultRedirectUrl;
    }

    @SneakyThrows
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        CustomOauthUser principal = (CustomOauthUser) authentication.getPrincipal();

        ServerWebExchange exchange = webFilterExchange.getExchange();
        OauthRequestState state = OauthRequestStateUtils.getRequestStateFromRequest(exchange);
        String redirectUrl = state.getSuccessRedirectUrl() != null
            ? buildSuccessRedirectUrl(state.getSuccessRedirectUrl(), principal)
            : defaultRedirectUrl;

        var redirectStrategy = new DefaultServerRedirectStrategy();
        URI location = URI.create(redirectUrl);
        return redirectStrategy.sendRedirect(exchange, location);
    }

    private String buildSuccessRedirectUrl(String baseUrl, CustomOauthUser principal) {
        return baseUrl
            + "?accessToken=" + principal.getAccessToken()
            + "&email=" + principal.getEmail();
    }
}