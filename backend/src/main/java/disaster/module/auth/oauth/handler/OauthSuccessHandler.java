package disaster.module.auth.oauth.handler;

import disaster.module.auth.oauth.CustomOauthUser;
import disaster.module.auth.oauth.state.OauthRequestState;
import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OauthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public OauthSuccessHandler() {
        super("/error");
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        CustomOauthUser principal = (CustomOauthUser) authToken.getPrincipal();
        OauthRequestState state = OauthRequestStateUtils.getRequestStateFromRequest(request);
        return state.getSuccessRedirectUrl() + "?accessToken" + principal.getAccessToken();
    }
}