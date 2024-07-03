package yelly.suda.domain.oauth2;

import static yelly.suda.domain.oauth2.SudaOAuth2Provider.GOOGLE;
import static yelly.suda.domain.oauth2.SudaOAuth2Provider.KAKAO;
import static yelly.suda.domain.oauth2.SudaOAuth2Provider.isGoogle;
import static yelly.suda.domain.oauth2.SudaOAuth2Provider.isKakao;

import io.jsonwebtoken.lang.Assert;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import yelly.suda.domain.member.Member;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SudaOAuth2Attribute {

    public static final String DIVIDER = "@";
    public static final String DEFAULT_ROLE = "OAUTH2_USER";
    private final String providerName;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String oAuthId;
    private final String username;
    private final String email;
    private final String profileImageUrl;

    protected static SudaOAuth2Attribute of(String providerName,
                                            String nameAttributeKey,
                                            Map<String, Object> attributes) {
        if (isKakao(providerName)) {
            return toKakaoAttribute(nameAttributeKey, attributes);
        }

        if (isGoogle(providerName)) {
            return toGoogleAttribute(nameAttributeKey, attributes);
        }

        throw new IllegalArgumentException("Unsupported provider: " + providerName);
    }

    private static SudaOAuth2Attribute toKakaoAttribute(String nameAttributeKey,
                                                        Map<String, Object> attributes) {
        // kakao 는 properties 라는 키를 가져야 한다.
        Assert.state(attributes.containsKey("properties"), "Kakao attributes must contain properties");

        @SuppressWarnings("unchecked") Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.putAll(attributes);
        newAttributes.putAll(properties);

        return SudaOAuth2Attribute.builder()
                .providerName(KAKAO.providerName())
                .nameAttributeKey(nameAttributeKey)
                .oAuthId((String) attributes.get(nameAttributeKey))
                .username((String) properties.get(KAKAO.username()))
                .email((String) properties.get(KAKAO.email()))
                .profileImageUrl((String) properties.get(KAKAO.profileImageUrl()))
                .attributes(newAttributes)
                .build();
    }

    private static SudaOAuth2Attribute toGoogleAttribute(String nameAttributeKey,
                                                         Map<String, Object> attributes) {
        return SudaOAuth2Attribute.builder()
                .providerName(GOOGLE.providerName())
                .nameAttributeKey(nameAttributeKey)
                .oAuthId((String) attributes.get(nameAttributeKey))
                .username((String) attributes.get(GOOGLE.username()))
                .email((String) attributes.get(GOOGLE.email()))
                .profileImageUrl((String) attributes.get(GOOGLE.profileImageUrl()))
                .attributes(attributes)
                .build();
    }

    public Member toMember() {
        return Member.builder()
                .oauthId(attributes.get(nameAttributeKey) + DIVIDER + providerName)
                .username(username)
                .email(email)
                .role(DEFAULT_ROLE)
                .build();
    }
}
