package yelly.suda.domain.oauth2;

import static yelly.suda.domain.oauth2.JwtConstants.CLAIM_ID;
import static yelly.suda.domain.oauth2.JwtConstants.CLAIM_PROFILE_IMAGE;
import static yelly.suda.domain.oauth2.JwtConstants.CLAIM_USERNAME;
import static yelly.suda.domain.oauth2.JwtConstants.JWT_EXPIRATION_TIME;
import static yelly.suda.domain.oauth2.JwtConstants.JWT_ISSUER;
import static yelly.suda.domain.oauth2.JwtConstants.JWT_REFRESH_EXPIRATION_TIME;
import static yelly.suda.domain.oauth2.JwtConstants.JWT_SUBJECT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

    @Value("#{environment['jwt-secret']}")
    private String JWT_SECRET;

    public String issueAccessToken(Map<String, Object> claims) {
        return issueToken(claims, JWT_EXPIRATION_TIME);
    }

    public String issueRefreshToken(Map<String, Object> claims) {
        return issueToken(claims, JWT_REFRESH_EXPIRATION_TIME);
    }

    private String issueToken(Map<String, Object> claims, int expirationTime) {
        return Jwts.builder()
                .issuer(JWT_ISSUER)
                .subject(JWT_SUBJECT)
                .claims(claims)
                .issuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .expiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(expirationTime)))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims getClaims(String jwt) throws Exception {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwt) // jwt 의 signClaim 부분을 파싱
                .getPayload(); // 페이로드 부분 읽기
    }

    public Map<String, Object> createMemberClaims(String id, String username, String profileImageUrl) {
        return Map.of(CLAIM_ID, id, CLAIM_USERNAME, username, CLAIM_PROFILE_IMAGE, profileImageUrl);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
