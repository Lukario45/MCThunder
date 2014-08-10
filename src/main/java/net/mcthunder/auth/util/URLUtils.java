package net.mcthunder.auth.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.exception.AuthenticationException;
import net.mcthunder.auth.exception.AuthenticationUnavailableException;
import net.mcthunder.auth.exception.InvalidCredentialsException;
import net.mcthunder.auth.exception.UserMigratedException;
import net.mcthunder.auth.properties.PropertyMap;
import net.mcthunder.auth.response.ProfileSearchResultsResponse;
import net.mcthunder.auth.response.Response;
import net.mcthunder.auth.serialize.GameProfileSerializer;
import net.mcthunder.auth.serialize.ProfileSearchResultsSerializer;
import net.mcthunder.auth.serialize.PropertyMapSerializer;
import net.mcthunder.auth.serialize.UUIDSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class URLUtils {

    private static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(GameProfile.class, new GameProfileSerializer());
        builder.registerTypeAdapter(PropertyMap.class, new PropertyMapSerializer());
        builder.registerTypeAdapter(UUID.class, new UUIDSerializer());
        builder.registerTypeAdapter(ProfileSearchResultsResponse.class, new ProfileSearchResultsSerializer());
        GSON = builder.create();
    }

    public static URL constantURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new Error("Malformed constant url: " + url);
        }
    }

    public static URL concatenateURL(URL url, String query) {
        try {
            return url.getQuery() != null && url.getQuery().length() > 0 ? new URL(url.getProtocol(), url.getHost(), url.getFile() + "&" + query) : new URL(url.getProtocol(), url.getHost(), url.getFile() + "?" + query);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Concatenated URL was malformed: " + url.toString() + ", " + query);
        }
    }

    public static String buildQuery(Map<String, Object> query) {
        if (query == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            Iterator<Entry<String, Object>> it = query.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Object> entry = it.next();
                if (builder.length() > 0) {
                    builder.append("&");
                }

                try {
                    builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }

                if (entry.getValue() != null) {
                    builder.append("=");
                    try {
                        builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }

            return builder.toString();
        }
    }

    public static <T extends Response> T makeRequest(URL url, Object input, Class<T> clazz) throws AuthenticationException {
        T result = null;
        try {
            String jsonString = input == null ? performGetRequest(url) : performPostRequest(url, GSON.toJson(input), "application/json");
            result = GSON.fromJson(jsonString, clazz);
        } catch (Exception e) {
            throw new AuthenticationUnavailableException("Could not make request to auth server.", e);
        }

        if (result == null) {
            return null;
        } else if (result.getError() != null && !result.getError().equals("")) {
            if (result.getCause() != null && result.getCause().equals("UserMigratedException")) {
                throw new UserMigratedException(result.getErrorMessage());
            } else if (result.getError().equals("ForbiddenOperationException")) {
                throw new InvalidCredentialsException(result.getErrorMessage());
            } else {
                throw new AuthenticationException(result.getErrorMessage());
            }
        } else {
            return result;
        }
    }

    private static HttpURLConnection createUrlConnection(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }

    private static String performPostRequest(URL url, String post, String type) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }

        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }

        HttpURLConnection connection = createUrlConnection(url);
        byte[] bytes = post.getBytes("UTF-8");
        connection.setRequestProperty("Content-Type", type + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", "" + bytes.length);
        connection.setDoOutput(true);
        OutputStream outputStream = null;
        try {
            outputStream = connection.getOutputStream();
            outputStream.write(bytes);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream == null) {
                throw e;
            }

            return IOUtils.toString(inputStream, "UTF-8");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private static String performGetRequest(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }

        HttpURLConnection connection = createUrlConnection(url);
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream == null) {
                throw e;
            }

            return IOUtils.toString(inputStream, "UTF-8");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
