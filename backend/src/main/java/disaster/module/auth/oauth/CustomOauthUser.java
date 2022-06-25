package disaster.module.auth.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauthUser implements OAuth2User {

    private final String accessToken;
    private final OAuth2User internalPrincipal;

    public CustomOauthUser(String accessToken, OAuth2User internalPrincipal) {
        this.accessToken = accessToken;
        this.internalPrincipal = internalPrincipal;
    }

    @Override
    public String getName() {
        return internalPrincipal.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return internalPrincipal.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return internalPrincipal.getAuthorities();
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
