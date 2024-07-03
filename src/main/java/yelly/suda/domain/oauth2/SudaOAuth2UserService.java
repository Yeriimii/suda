package yelly.suda.domain.oauth2;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yelly.suda.domain.member.Member;
import yelly.suda.domain.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class SudaOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = loadOAuth2User(userRequest);

        String providerName = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName(); // 고유 식별자를 찾는 키

        SudaOAuth2Attribute attribute = SudaOAuth2Attribute.of(providerName, userNameAttributeName,
                oAuth2User.getAttributes());

        Member member = saveOrUpdate(attribute);

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole())),
                attribute.getAttributes(),
                attribute.getNameAttributeKey()
        );
    }

    private OAuth2User loadOAuth2User(OAuth2UserRequest userRequest) {
        return oAuth2UserService.loadUser(userRequest);
    }

    private Member saveOrUpdate(SudaOAuth2Attribute attribute) {
        Member member = memberRepository.findByOauthId(attribute.getOAuthId())
                .orElse(attribute.toMember());

        // OAuth 유저의 이름이 변경되었다면 멤버의 username 업데이트
        String memberUsername = member.getUsername();
        String oAuthUserName = attribute.getUsername();
        if (!memberUsername.equals(oAuthUserName)) {
            return member.changeUsername(oAuthUserName);
        }

        // 새로운 멤버 저장 및 반환
        return memberRepository.save(member);
    }
}
