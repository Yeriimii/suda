package yelly.suda.domain.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import yelly.suda.domain.member.Member;

class SudaOAuth2AttributeTest {

    @DisplayName("존재하는 provider 이름인 kakao, google 로 SudaOAuth2Attribute 객체를 생성할 수 있다.")
    @ParameterizedTest(name = "provider: {0}")
    @ValueSource(strings = {"kakao", "google"})
    void can_create_SudaOAuth2Attribute_with_existing_providers(String providerName) {
        // given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("properties", new HashMap<>());

        // when
        SudaOAuth2Attribute oAuth2Attribute = SudaOAuth2Attribute.of(providerName, null, attributes);

        // then
        assertThat(oAuth2Attribute.getProviderName()).isEqualTo(providerName);
    }

    @DisplayName("kakao, google 외 존재하지 않는 provider 이름으로 생성하면 IllegalArgumentException 예외가 발생한다.")
    @ParameterizedTest(name = "provider: {0}")
    @ValueSource(strings = {"KAKAO", "GOOGLE"})
    void create_SudaOAuth2Attribute_with_invalid_provider_name_throws_exception(String providerName) {
        // given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("properties", new HashMap<>());

        // when & then
        assertThatThrownBy(() -> SudaOAuth2Attribute.of(providerName, null, attributes))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("kakao 에 대해 'properties' 키가 없는 attribute 로 생성하면 IllegalStateException 예외가 발생한다.")
    @ParameterizedTest(name = "provider: {0}")
    @ValueSource(strings = {"kakao"})
    void create_SudaOAuth2Attribute_with_invalid_attributes_throws_exception(String providerName) {
        // given
        Map<String, Object> attributes = new HashMap<>();

        // when & then
        assertThatThrownBy(() -> SudaOAuth2Attribute.of(providerName, null, attributes))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("고유 아이디가 123456789 이며, tester 라는 이름을 가진 google 유저로 로그인하면 아이디에 접미사로 '@google' 가 붙는다.")
    void toMember_with_unique_id_and_name_log_in_with_google() {
        // given
        String providerName = "google";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "123456789"); // OAuth2 ID
        attributes.put("name", "tester"); // USERNAME

        SudaOAuth2Attribute googleAttribute = SudaOAuth2Attribute.of(providerName, "id", attributes);

        // when
        Member googleOAuthUser = googleAttribute.toMember();

        // then
        assertThat(googleOAuthUser.getOAuthId()).isEqualTo("123456789@google");
        assertThat(googleOAuthUser.getUsername()).isEqualTo("tester");
        assertThat(googleOAuthUser.getRole()).isEqualTo("OAUTH2_USER");
    }
}