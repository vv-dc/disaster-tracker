package disaster.module.auth.oauth.state;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OauthRequestState {

    String successRedirectUrl;
    String failureRedirectUrl;
}