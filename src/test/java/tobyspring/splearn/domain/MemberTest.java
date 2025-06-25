package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    private PasswordEncoder passwordEncoder;
    private Member member;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(final String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(final String password, final String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
        member = Member.create("posty@splearn.app", "Posty", "secret", passwordEncoder);
    }

    @DisplayName("회원을 생성하면 PENDING(가입 대기) 상태다.")
    @Test
    void createMember() {
        // when & then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);
    }

    @DisplayName("가입 대기(PENDING) 상태의 회원을 가입 완료시킬 수 있다.")
    @Test
    void activate() {
        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.ACTIVE);
    }

    @DisplayName("가입 대기(PENDING) 상태가 아니면 회원을 가입 완료시킬 수 없다.")
    @Test
    void activateFail() {
        // given
        member.activate();

        // when & then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("가입 완료(ACTIVATE) 상태의 회원을 탈퇴시킬 수 있다.")
    @Test
    void deactivate() {
        // given
        member.activate();

        // when
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.DEACTIVATED);
    }

    @DisplayName("가입 대기 상태의 회원을 탈퇴시킬 수 없다.")
    @Test
    void deactivateFailWhenPending() {
        // when & then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("탈퇴 상태의 회원을 탈퇴시킬 수 없다.")
    @Test
    void deactivateFailWhenDeactivated() {
        // given
        member.activate();
        member.deactivate();

        // when & then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("비밀번호가 회원의 패스워드 해시값과 일치하는지 확인한다.")
    @CsvSource(value = {
            "secret,true",
            "hello,false"
    })
    @ParameterizedTest
    void verifyPassword(String password, boolean expected) {
        assertThat(member.verifyPassword(password, passwordEncoder)).isEqualTo(expected);
    }

    @DisplayName("changeNickname")
    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("Posty");

        member.changeNickname("Bokdeok");

        assertThat(member.getNickname()).isEqualTo("Bokdeok");
    }

    @DisplayName("changePassword")
    @Test
    void changePassword() {
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }
}
