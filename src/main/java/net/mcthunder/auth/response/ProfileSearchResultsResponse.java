package net.mcthunder.auth.response;

import net.mcthunder.auth.GameProfile;

public class ProfileSearchResultsResponse extends Response {

    private GameProfile[] profiles;

    public GameProfile[] getProfiles() {
        return this.profiles;
    }

    public void setProfiles(GameProfile[] profiles) {
        this.profiles = profiles;
    }

}
