package yelly.suda.domain.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static yelly.suda.domain.oauth2.JwtConstants.CLAIM_ID;
import static yelly.suda.domain.oauth2.JwtConstants.CLAIM_PROFILE_IMAGE;
import static yelly.suda.domain.oauth2.JwtConstants.CLAIM_USERNAME;

import io.jsonwebtoken.Claims;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtTokenService.class)
@TestPropertySource(properties = "jwt-secret=PqAJ6ueeuliHnuYL5GWZrjJiUyggFatAgYG6nFcwLZ0=")
class JwtTokenServiceTest {

    private static final String ID = "1234";
    private static final String USERNAME = "yelly";
    private static final String PROFILE_IMAGE = "no-image";

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    @DisplayName("id 가 1234, username 이 yelly, profileImageUrl 이 no-images 인 Claim 을 생성할 수 있다.")
    void createMemberClaims() {
        // given
        String id = "1234";
        String username = "yelly";
        String profileImage = "no-images";

        // when
        Map<String, Object> memberClaims = jwtTokenService.createMemberClaims(id, username, profileImage);

        // then
        assertThat(memberClaims.get(CLAIM_ID)).isEqualTo(id);
        assertThat(memberClaims.get(CLAIM_USERNAME)).isEqualTo(username);
        assertThat(memberClaims.get(CLAIM_PROFILE_IMAGE)).isEqualTo(profileImage);
    }

    @Test
    @DisplayName("생성된 claim 을 통해 jwt 를 발행할 수 있다.")
    void issueAccessToken() throws Exception {
        // given
        Map<String, Object> memberClaims = jwtTokenService.createMemberClaims(ID, USERNAME, PROFILE_IMAGE);

        // when
        String jwt = jwtTokenService.issueAccessToken(memberClaims);
        Claims claims = jwtTokenService.getClaims(jwt);

        // then
        assertThat(jwt).isNotBlank();
        assertThat(memberClaims.get(CLAIM_ID)).isEqualTo(ID);
        assertThat(memberClaims.get(CLAIM_USERNAME)).isEqualTo(USERNAME);
        assertThat(memberClaims.get(CLAIM_PROFILE_IMAGE)).isEqualTo(PROFILE_IMAGE);
    }

    @Test
    @DisplayName("생성된 claim 을 통해 refresh token 을 발행할 수 있다.")
    void issueRefreshToken() {
        // given
        Map<String, Object> memberClaims = jwtTokenService.createMemberClaims(ID, USERNAME, PROFILE_IMAGE);

        // when
        String jwt = jwtTokenService.issueRefreshToken(memberClaims);

        // then
        assertThat(jwt).isNotBlank();
        assertThat(memberClaims.get(CLAIM_ID)).isEqualTo(ID);
        assertThat(memberClaims.get(CLAIM_USERNAME)).isEqualTo(USERNAME);
        assertThat(memberClaims.get(CLAIM_PROFILE_IMAGE)).isEqualTo(PROFILE_IMAGE);
    }

    @Test
    @DisplayName("id 가 1234, username 이 yelly, profileImageUrl 이 no-images 인 Claim 을 파싱할 수 있다.")
    void getClaims() throws Exception {
        // given
        Map<String, Object> memberClaims = jwtTokenService.createMemberClaims(ID, USERNAME, PROFILE_IMAGE);

        // 토큰 발행
        String jwt = jwtTokenService.issueAccessToken(memberClaims);

        // when
        Claims claims = jwtTokenService.getClaims(jwt);

        // then
        assertThat(claims.get(CLAIM_ID)).isEqualTo(ID);
        assertThat(claims.get(CLAIM_USERNAME)).isEqualTo(USERNAME);
        assertThat(claims.get(CLAIM_PROFILE_IMAGE)).isEqualTo(PROFILE_IMAGE);
    }
}