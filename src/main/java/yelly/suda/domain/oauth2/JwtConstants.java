package yelly.suda.domain.oauth2;

public interface JwtConstants {

    /* jwt response header */
    String JWT_ACCESS_HEADER = "Authorization";
    String JWT_REFRESH_HEADER = "Jwt-Refresh-Token";

    /* jwt issuer, subject */
    String JWT_ISSUER = "suda";
    String JWT_SUBJECT = "chat";

    /* jwt member claims */
    String CLAIM_ID = "id";
    String CLAIM_USERNAME = "username";
    String CLAIM_PROFILE_IMAGE = "profileImageUrl";

    /* jwt expiration times */
    int JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    int JWT_REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

    /* cookie key */
    String JWT_ACCESS_COOKIE_KEY = "jwt-access-token";
    String JWT_REFRESH_COOKIE_KEY = "jwt-refresh-token";

    /* cookie expiration times */
    int COOKIE_EXPIRATION_TIME = 10;
}
