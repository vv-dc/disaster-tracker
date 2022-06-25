package disaster.module.auth.oauth.handler;

import disaster.module.auth.oauth.state.OauthRequestState;
import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OauthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public OauthFailureHandler() {
        super("/error");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        OauthRequestState state = OauthRequestStateUtils.getRequestStateFromRequest(request);
        String redirectUrl = state.getFailureRedirectUrl();
        super.getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
