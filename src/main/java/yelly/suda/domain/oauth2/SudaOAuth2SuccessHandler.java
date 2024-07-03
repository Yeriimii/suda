package yelly.suda.domain.oauth2;

import static yelly.suda.domain.oauth2.JwtConstants.COOKIE_EXPIRATION_TIME;
import static yelly.suda.domain.oauth2.JwtConstants.JWT_ACCESS_COOKIE_KEY;
import static yelly.suda.domain.oauth2.JwtConstants.JWT_REFRESH_COOKIE_KEY;
import static yelly.suda.domain.oauth2.SudaOAuth2Provider.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SudaOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;

    @Value("#{environment['allow-origin']}")
    private String allowOrigin;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        /* member claims */
        Map<String, Object> claims = createClaims(authentication);

        /* jwt-access-token */
        String jwt = jwtTokenService.issueAccessToken(claims);
        Cookie jwtCookie = createJwtCookie(JWT_ACCESS_COOKIE_KEY, jwt);
        response.addCookie(jwtCookie);

        /* jwt-refresh-token */
        String refreshToken = jwtTokenService.issueRefreshToken(claims);
        Cookie refreshCookie = createJwtCookie(JWT_REFRESH_COOKIE_KEY, refreshToken);
        response.addCookie(refreshCookie);

        response.sendRedirect(allowOrigin);
    }

    private Map<String, Object> createClaims(Authentication authentication) {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();
        String id = oAuth2User.getName(); // OAuth 유저의 고유 아이디
        String username = "";
        String profileImage = "";

        /* OAuth provider 분기 처리 */
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        if (isGoogle(registrationId.toLowerCase())) {
            username = oAuth2UserAttributes.get(GOOGLE.username()).toString();
            profileImage = oAuth2UserAttributes.get(GOOGLE.profileImageUrl()).toString();
        }

        if (isKakao(registrationId.toLowerCase())) {
            username = oAuth2UserAttributes.get(KAKAO.username()).toString();
            profileImage = oAuth2UserAttributes.get(KAKAO.profileImageUrl()).toString();
        }

        return jwtTokenService.createMemberClaims(id, username, profileImage);
    }

    private Cookie createJwtCookie(String cookieKey, String jwt) {
        Cookie cookie = new Cookie(cookieKey, jwt);
        cookie.setPath("/");
        cookie.setSecure(false); // TODO: 개발 중에만 사용
        cookie.setHttpOnly(false);
        cookie.setMaxAge(COOKIE_EXPIRATION_TIME);

        return cookie;
    }
}
