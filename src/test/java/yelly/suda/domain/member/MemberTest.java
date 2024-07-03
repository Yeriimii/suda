package yelly.suda.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("yelly 라는 username 을 갖는 멤버를 생성할 수 있다")
    @Test
    void create_member() {
        // given
        String username = "yelly";
        Member member = Member.builder()
                .username(username)
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getUsername()).isEqualTo(username);
    }
}