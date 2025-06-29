package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.MemberFixture.createPasswordEncoder;

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
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterRequest(), passwordEncoder);
    }

    @DisplayName("회원을 등록하면 PENDING(등록 대기) 상태다.")
    @Test
    void registerMember() {
        // when & then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);
    }

    @DisplayName("등록 대기(PENDING) 상태의 회원을 등록 완료시킬 수 있다.")
    @Test
    void activate() {
        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.ACTIVE);
    }

    @DisplayName("등록 대기(PENDING) 상태가 아니면 회원을 등록 완료시킬 수 없다.")
    @Test
    void activateFail() {
        // given
        member.activate();

        // when & then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("등록 완료(ACTIVATE) 상태의 회원을 탈퇴시킬 수 있다.")
    @Test
    void deactivate() {
        // given
        member.activate();

        // when
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.DEACTIVATED);
    }

    @DisplayName("등록 대기 상태의 회원을 탈퇴시킬 수 없다.")
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

    @DisplayName("회원의 닉네임을 변경한다.")
    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("Posty");

        member.changeNickname("Bokdeok");

        assertThat(member.getNickname()).isEqualTo("Bokdeok");
    }

    @DisplayName("회원의 비밀번호를 변경한다.")
    @Test
    void changePassword() {
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
    }

    @DisplayName("회원의 활성 상태를 확인한다.")
    @Test
    void shouldBeActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @DisplayName("유효하지 않은 이메일로 회원을 등록할 수 없다.")
    @Test
    void invalidEmail() {
        assertThatThrownBy(() ->
                Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
