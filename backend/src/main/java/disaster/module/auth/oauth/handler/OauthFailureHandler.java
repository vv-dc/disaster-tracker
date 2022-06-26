package disaster.module.auth.oauth.handler;

import disaster.module.auth.oauth.state.OauthRequestState;
import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class OauthFailureHandler extends RedirectServerAuthenticationFailureHandler {

    private final String defaultRedirectUrl;
    private final ServerRedirectStrategy redirectStrategy;

    public OauthFailureHandler(String defaultRedirectUrl) {
        super(defaultRedirectUrl);
        this.defaultRedirectUrl = defaultRedirectUrl;
        this.redirectStrategy = new DefaultServerRedirectStrategy();
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        OauthRequestState state = OauthRequestStateUtils.getRequestStateFromRequest(exchange);

        String redirectUrl = (state.getFailureRedirectUrl() != null
            ? state.getFailureRedirectUrl()
            : defaultRedirectUrl) + "?reason=" + exception.getMessage();

        var location = URI.create(redirectUrl);
        return redirectStrategy.sendRedirect(exchange, location);
    }
}
