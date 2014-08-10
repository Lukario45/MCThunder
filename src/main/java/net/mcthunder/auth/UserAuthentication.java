package net.mcthunder.auth;

import net.mcthunder.auth.expection.AuthenticationException;
import net.mcthunder.auth.expection.InvalidCredentialsException;
import net.mcthunder.auth.expection.PropertyDeserializeException;
import net.mcthunder.auth.properties.Property;
import net.mcthunder.auth.properties.PropertyMap;
import net.mcthunder.auth.request.AuthenticationRequest;
import net.mcthunder.auth.request.RefreshRequest;
import net.mcthunder.auth.response.AuthenticationResponse;
import net.mcthunder.auth.response.RefreshResponse;
import net.mcthunder.auth.response.User;
import net.mcthunder.auth.serialize.UUIDSerializer;
import net.mcthunder.auth.util.URLUtils;

import java.net.URL;
import java.util.*;

public class UserAuthentication {
    private static final String BASE_URL = "https://authserver.mojang.com/";
    private static final URL ROUTE_AUTHENTICATE = URLUtils.constantURL("https://authserver.mojang.com/authenticate");
    private static final URL ROUTE_REFRESH = URLUtils.constantURL("https://authserver.mojang.com/refresh");
    private static final String STORAGE_KEY_PROFILE_NAME = "displayName";
    private static final String STORAGE_KEY_PROFILE_ID = "uuid";
    private static final String STORAGE_KEY_PROFILE_PROPERTIES = "profileProperties";
    private static final String STORAGE_KEY_USER_NAME = "username";
    private static final String STORAGE_KEY_USER_ID = "userid";
    private static final String STORAGE_KEY_USER_PROPERTIES = "userProperties";
    private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
    private String clientToken;
    private PropertyMap userProperties = new PropertyMap();
    private String userId;
    private String username;
    private String password;
    private String accessToken;
    private boolean isOnline;
    private List<GameProfile> profiles = new ArrayList();
    private GameProfile selectedProfile;
    private UserType userType;

    public UserAuthentication(String clientToken) {
        if (clientToken == null) {
            throw new IllegalArgumentException("ClientToken cannot be null.");
        }

        this.clientToken = clientToken;
    }

    public String getClientToken() {
        return this.clientToken;
    }

    public String getUserID() {
        return this.userId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        if ((isLoggedIn()) && (canPlayOnline())) {
            throw new IllegalStateException("Cannot change accessToken whilst logged in & online");
        }
        this.accessToken = accessToken;
    }

    public List<GameProfile> getAvailableProfiles() {
        return this.profiles;
    }

    public GameProfile getSelectedProfile() {
        return this.selectedProfile;
    }

    public UserType getUserType() {
        return isLoggedIn() ? this.userType : this.userType == null ? UserType.LEGACY : null;
    }

    public PropertyMap getUserProperties() {
        return isLoggedIn() ? new PropertyMap(this.userProperties) : new PropertyMap();
    }

    public boolean isLoggedIn() {
        return (this.accessToken != null) && (!this.accessToken.equals(""));
    }

    public boolean canPlayOnline() {
        return (isLoggedIn()) && (getSelectedProfile() != null) && (this.isOnline);
    }

    public boolean canLogIn() {
        return (!canPlayOnline()) && (this.username != null) && (!this.username.equals("")) && (((this.password != null) && (!this.password.equals(""))) || ((this.accessToken != null) && (!this.accessToken.equals(""))));
    }

    public void setUsername(String username) {
        if ((isLoggedIn()) && (canPlayOnline())) {
            throw new IllegalStateException("Cannot change username whilst logged in & online");
        }
        this.username = username;
    }

    public void setPassword(String password) {
        if ((isLoggedIn()) && (canPlayOnline()) && (this.password != null) && (!this.password.equals(""))) {
            throw new IllegalStateException("Cannot set password whilst logged in & online");
        }
        this.password = password;
    }

    public void loadFromStorage(Map<String, Object> credentials) throws PropertyDeserializeException {
        logout();
        setUsername((String) credentials.get("username"));
        if (credentials.containsKey("userid"))
            this.userId = ((String) credentials.get("userid"));
        else {
            this.userId = this.username;
        }

        if (credentials.containsKey("userProperties")) {
            try {
                List list = (List) credentials.get("userProperties");
                for (Map propertyMap : list) {
                    String name = (String) propertyMap.get("name");
                    String value = (String) propertyMap.get("value");
                    String signature = (String) propertyMap.get("signature");
                    if (signature == null)
                        this.userProperties.put(name, new Property(name, value));
                    else
                        this.userProperties.put(name, new Property(name, value, signature));
                }
            } catch (Throwable t) {
                throw new PropertyDeserializeException("Couldn't deserialize user properties", t);
            }
        }

        if ((credentials.containsKey("displayName")) && (credentials.containsKey("uuid"))) {
            GameProfile profile = new GameProfile(UUIDSerializer.fromString((String) credentials.get("uuid")), (String) credentials.get("displayName"));
            if (credentials.containsKey("profileProperties")) {
                try {
                    List list = (List) credentials.get("profileProperties");
                    for (Map propertyMap : list) {
                        String name = (String) propertyMap.get("name");
                        String value = (String) propertyMap.get("value");
                        String signature = (String) propertyMap.get("signature");
                        if (signature == null)
                            profile.getProperties().put(name, new Property(name, value));
                        else
                            profile.getProperties().put(name, new Property(name, value, signature));
                    }
                } catch (Throwable t) {
                    throw new PropertyDeserializeException("Couldn't deserialize profile properties", t);
                }
            }

            this.selectedProfile = profile;
        }

        this.accessToken = ((String) credentials.get("accessToken"));
    }

    public Map<String, Object> saveForStorage() {
        Map result = new HashMap();
        if (this.username != null) {
            result.put("username", this.username);
        }

        if (getUserID() != null) {
            result.put("userid", this.userId);
        }

        if (!getUserProperties().isEmpty()) {
            List properties = new ArrayList();
            for (Property userProperty : getUserProperties().values()) {
                Map property = new HashMap();
                property.put("name", userProperty.getName());
                property.put("value", userProperty.getValue());
                property.put("signature", userProperty.getSignature());
                properties.add(property);
            }

            result.put("userProperties", properties);
        }

        GameProfile selectedProfile = getSelectedProfile();
        if (selectedProfile != null) {
            result.put("displayName", selectedProfile.getName());
            result.put("uuid", selectedProfile.getId());
            List properties = new ArrayList();
            for (Property profileProperty : selectedProfile.getProperties().values()) {
                Map property = new HashMap();
                property.put("name", profileProperty.getName());
                property.put("value", profileProperty.getValue());
                property.put("signature", profileProperty.getSignature());
                properties.add(property);
            }

            if (!properties.isEmpty()) {
                result.put("profileProperties", properties);
            }
        }

        if ((this.accessToken != null) && (!this.accessToken.equals(""))) {
            result.put("accessToken", this.accessToken);
        }

        return result;
    }

    public void login() throws AuthenticationException {
        if ((this.username == null) || (this.username.equals(""))) {
            throw new InvalidCredentialsException("Invalid username");
        }
        if ((this.accessToken != null) && (!this.accessToken.equals(""))) {
            loginWithToken();
        } else {
            if ((this.password == null) || (this.password.equals(""))) {
                throw new InvalidCredentialsException("Invalid password");
            }

            loginWithPassword();
        }
    }

    private void loginWithPassword() throws AuthenticationException {
        if ((this.username == null) || (this.username.equals("")))
            throw new InvalidCredentialsException("Invalid username");
        if ((this.password == null) || (this.password.equals(""))) {
            throw new InvalidCredentialsException("Invalid password");
        }
        AuthenticationRequest request = new AuthenticationRequest(this, this.username, this.password);
        AuthenticationResponse response = (AuthenticationResponse) URLUtils.makeRequest(ROUTE_AUTHENTICATE, request, AuthenticationResponse.class);
        if (!response.getClientToken().equals(getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
        }
        if (response.getSelectedProfile() != null)
            this.userType = (response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        else if ((response.getAvailableProfiles() != null) && (response.getAvailableProfiles().length != 0)) {
            this.userType = (response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        }

        if ((response.getUser() != null) && (response.getUser().getId() != null))
            this.userId = response.getUser().getId();
        else {
            this.userId = this.username;
        }

        this.isOnline = true;
        this.accessToken = response.getAccessToken();
        this.profiles = Arrays.asList(response.getAvailableProfiles());
        this.selectedProfile = response.getSelectedProfile();
        updateProperties(response.getUser());
    }

    private void loginWithToken()
            throws AuthenticationException {
        if ((this.userId == null) || (this.userId.equals(""))) {
            if ((this.username == null) || (this.username.equals(""))) {
                throw new InvalidCredentialsException("Invalid uuid & username");
            }

            this.userId = this.username;
        }

        if ((this.accessToken == null) || (this.accessToken.equals(""))) {
            throw new InvalidCredentialsException("Invalid access token");
        }
        RefreshRequest request = new RefreshRequest(this);
        RefreshResponse response = (RefreshResponse) URLUtils.makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
        if (!response.getClientToken().equals(getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
        }
        if (response.getSelectedProfile() != null)
            this.userType = (response.getSelectedProfile().isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        else if ((response.getAvailableProfiles() != null) && (response.getAvailableProfiles().length != 0)) {
            this.userType = (response.getAvailableProfiles()[0].isLegacy() ? UserType.LEGACY : UserType.MOJANG);
        }

        if ((response.getUser() != null) && (response.getUser().getId() != null))
            this.userId = response.getUser().getId();
        else {
            this.userId = this.username;
        }

        this.isOnline = true;
        this.accessToken = response.getAccessToken();
        this.profiles = Arrays.asList(response.getAvailableProfiles());
        this.selectedProfile = response.getSelectedProfile();
        updateProperties(response.getUser());
    }

    public void logout() {
        this.password = null;
        this.userId = null;
        this.selectedProfile = null;
        this.userProperties.clear();
        this.accessToken = null;
        this.profiles = null;
        this.isOnline = false;
        this.userType = null;
    }

    public void selectGameProfile(GameProfile profile) throws AuthenticationException {
        if (!isLoggedIn())
            throw new AuthenticationException("Cannot change game profile whilst not logged in");
        if (getSelectedProfile() != null)
            throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
        if ((profile != null) && (this.profiles.contains(profile))) {
            RefreshRequest request = new RefreshRequest(this, profile);
            RefreshResponse response = (RefreshResponse) URLUtils.makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
            if (!response.getClientToken().equals(getClientToken())) {
                throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
            }
            this.isOnline = true;
            this.accessToken = response.getAccessToken();
            this.selectedProfile = response.getSelectedProfile();
        } else {
            throw new IllegalArgumentException("Invalid profile '" + profile + "'");
        }
    }

    public String toString() {
        return "UserAuthentication{profiles=" + this.profiles + ", selectedProfile=" + getSelectedProfile() + ", username=" + this.username + ", isLoggedIn=" + isLoggedIn() + ", canPlayOnline=" + canPlayOnline() + ", accessToken=" + this.accessToken + ", clientToken=" + getClientToken() + "}";
    }

    private void updateProperties(User user) {
        this.userProperties.clear();
        if ((user != null) && (user.getProperties() != null))
            this.userProperties.putAll(user.getProperties());
    }
}