package disaster.module.auth.oauth.state;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;

public class OauthRequestStateUtils {

    private static final String SUCCESS_REDIRECT_URL_PARAM_NAME = "disaster_success_url";
    private static final String FAILURE_REDIRECT_URL_PARAM_NAME = "disaster_failure_url";
    private static final String STATE_PARAM_NAME = "disasterRequest_state";

    public static HashMap<String, Object> buildAdditionalParamsFromRequest(ServerWebExchange exchange, OAuth2AuthorizationRequest context) {
        var additionalParams = new HashMap<>(context.getAdditionalParameters());
        var queryParams = exchange.getRequest().getQueryParams();

        String successRedirectUrl = queryParams.getFirst(SUCCESS_REDIRECT_URL_PARAM_NAME);
        String failureRedirectUrl = queryParams.getFirst(FAILURE_REDIRECT_URL_PARAM_NAME);
        var state = new OauthRequestState(successRedirectUrl, failureRedirectUrl);

        additionalParams.put(STATE_PARAM_NAME, state);
        return additionalParams;
    }

    public static void setOauthStateToRequest(ServerWebExchange exchange, OAuth2AuthorizationRequest context) {
        OauthRequestState state = (OauthRequestState) context.getAdditionalParameters().get(STATE_PARAM_NAME);
        exchange.getAttributes().put(STATE_PARAM_NAME, state);
    }

    public static OauthRequestState getRequestStateFromRequest(ServerWebExchange exchange) {
        return (OauthRequestState) exchange.getAttributes().get(STATE_PARAM_NAME);
    }
}
