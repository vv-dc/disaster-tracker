package disaster.module.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> internalUserService = new DefaultOAuth2UserService();

    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2AccessToken token = userRequest.getAccessToken();
        OAuth2User user = internalUserService.loadUser(userRequest);
        return new CustomOauthUser(token.getTokenValue(), user);
    }
}
