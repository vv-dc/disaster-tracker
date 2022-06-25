package disaster.module.auth.oauth;

import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomOauthRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> internalRepository;

    public CustomOauthRequestRepository() {
        this.internalRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return internalRepository.loadAuthorizationRequest(request);
    }

    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        internalRepository.saveAuthorizationRequest(authorizationRequest, request, response);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        throw new RuntimeException("Deprecated");
    }

    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        var context = internalRepository.removeAuthorizationRequest(request, response);
        if (context != null) {
            OauthRequestStateUtils.setOauthStateToRequest(request, context);
        }
        return context;
    }
}
