package yelly.suda.domain.oauth2;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SudaOAuth2Provider {
    KAKAO("kakao", "nickName", "account_email", "profile_image"),
    GOOGLE("google", "name", "email", "picture");

    private final String providerName;
    private final String username;
    private final String email;
    private final String profileImageUrl;

    public String providerName() {
        return providerName;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }

    public static boolean isKakao(String providerName) {
        validate(providerName);
        return KAKAO.providerName.equals(providerName);
    }

    public static boolean isGoogle(String providerName) {
        validate(providerName);
        return GOOGLE.providerName.equals(providerName);
    }

    private static boolean isSupported(String providerName) {
        return Arrays.stream(SudaOAuth2Provider.values())
                .anyMatch(sudaOAuth2Provider -> sudaOAuth2Provider.providerName.equals(providerName));
    }

    private static void validate(String providerName) {
        if (!isSupported(providerName)) {
            throw new IllegalArgumentException(providerName + " is not supported");
        }
    }
}
