package disaster.module.auth.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauthUser implements OAuth2User {

    private final String accessToken;
    private final String email;
    private final OAuth2User internalPrincipal;

    public CustomOauthUser(String accessToken, OAuth2User internalPrincipal) {
        this.accessToken = accessToken;
        this.internalPrincipal = internalPrincipal;
        this.email = (String) internalPrincipal.getAttributes().get(StandardClaimNames.EMAIL);
    }

    @Override
    public String getName() {
        return internalPrincipal.getName();
    }

    public String getEmail() {
        return email;
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
