package net.mcthunder.auth.request;

import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.UserAuthentication;

public class RefreshRequest {
    private String clientToken;
    private String accessToken;
    private GameProfile selectedProfile;
    private boolean requestUser;

    public RefreshRequest(UserAuthentication authService) {
        this(authService, null);
    }

    public RefreshRequest(UserAuthentication authService, GameProfile profile) {
        this.requestUser = true;
        this.clientToken = authService.getClientToken();
        this.accessToken = authService.getAccessToken();
        this.selectedProfile = profile;
    }
}