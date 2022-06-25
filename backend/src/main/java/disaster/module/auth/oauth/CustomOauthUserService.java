package disaster.module.auth.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import reactor.core.publisher.Mono;

public class CustomOauthUserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> internalUserService = new DefaultReactiveOAuth2UserService();

    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) {
        OAuth2AccessToken token = userRequest.getAccessToken();
        return internalUserService.loadUser(userRequest)
            .map((user) -> new CustomOauthUser(token.getTokenValue(), user));
    }
}
