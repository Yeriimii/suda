package yelly.suda.domain.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static yelly.suda.domain.oauth2.SudaOAuth2Provider.isGoogle;
import static yelly.suda.domain.oauth2.SudaOAuth2Provider.isKakao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SudaOAuth2ProviderTest {

    @DisplayName("kakao 와 google 일 때 true 또는 false 를 반환한다.")
    @ParameterizedTest(name = "{0} 이면 {1} 를 반환한다.")
    @CsvSource(value = {"kakao,true", "google,false"})
    void isKakao_with_exist_provider(String providerName, Boolean expectedResult) {
        // given & when
        boolean actualResult = isKakao(providerName);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("대소문자를 구분하며, 존재하지 않는 provider 는 IllegalArgumentException 예외를 던진다.")
    @ParameterizedTest(name = "provider name: {0}")
    @ValueSource(strings = {"Kakao", "Google"})
    void isKakao_without_exist_provider(String providerName) {
        Assertions.assertThatThrownBy(() -> isKakao(providerName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("kakao 와 google 일 때 true 또는 false 를 반환한다.")
    @ParameterizedTest(name = "{0} 이면 {1} 를 반환한다.")
    @CsvSource(value = {"kakao,false", "google,true"})
    void isGoogle_with_exist_provider(String providerName, Boolean expectedResult) {
        // given & when
        boolean actualResult = isGoogle(providerName);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("대소문자를 구분하며, 존재하지 않는 provider 는 IllegalArgumentException 예외를 던진다.")
    @ParameterizedTest(name = "provider name: {0}")
    @ValueSource(strings = {"Kakao", "Google"})
    void isGoogle_without_exist_provider(String providerName) {
        Assertions.assertThatThrownBy(() -> isGoogle(providerName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}