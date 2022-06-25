package disaster.module.auth.oauth.state;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class OauthRequestStateUtils {

    private static final String SUCCESS_REDIRECT_URL_PARAM_NAME = "disaster_success_url";
    private static final String FAILURE_REDIRECT_URL_PARAM_NAME = "disaster_failure_url";
    private static final String STATE_PARAM_NAME = "disasterRequest_state";

    public static HashMap<String, Object> buildAdditionalParamsFromRequest(HttpServletRequest request, OAuth2AuthorizationRequest context) {
        var additionalParams = new HashMap<>(context.getAdditionalParameters());

        String successRedirectUrl = request.getParameter(SUCCESS_REDIRECT_URL_PARAM_NAME);
        String failureRedirectUrl = request.getParameter(FAILURE_REDIRECT_URL_PARAM_NAME);
        var state = new OauthRequestState(successRedirectUrl, failureRedirectUrl);

        additionalParams.put(STATE_PARAM_NAME, state);
        return additionalParams;
    }

    public static void setOauthStateToRequest(HttpServletRequest request, OAuth2AuthorizationRequest context) {
        OauthRequestState state = (OauthRequestState) context.getAdditionalParameters().get(STATE_PARAM_NAME);
        request.setAttribute(STATE_PARAM_NAME, state);
    }

    public static OauthRequestState getRequestStateFromRequest(HttpServletRequest request) {
        return (OauthRequestState) request.getAttribute(STATE_PARAM_NAME);
    }
}
