package disaster.module.auth.oauth;

import disaster.module.auth.oauth.state.OauthRequestStateUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;

public class CustomOauthRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver internalResolver;

    public CustomOauthRequestResolver(ClientRegistrationRepository repository, String resolveUrl) {
        this.internalResolver = new DefaultOAuth2AuthorizationRequestResolver(repository, resolveUrl);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest context = internalResolver.resolve(request);
        if (context == null) {
            return null;
        }
        return decorateContext(request, context);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
        OAuth2AuthorizationRequest context = internalResolver.resolve(request, registrationId);
        if (context == null) {
            return null;
        }
        return decorateContext(request, context);
    }

    private OAuth2AuthorizationRequest decorateContext(HttpServletRequest request, OAuth2AuthorizationRequest context) {
        var additionalParams = OauthRequestStateUtils.buildAdditionalParamsFromRequest(request, context);
        return OAuth2AuthorizationRequest
            .from(context)
            .additionalParameters(additionalParams)
            .build();
    }
}
