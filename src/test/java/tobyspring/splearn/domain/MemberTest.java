package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("회원을 생성하면 PENDING(가입 대기) 상태다.")
    @Test
    void createMember() {
        // given
        var member = new Member("posty@splearn.app", "Posty", "secret");

        // when & then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);
    }

    @DisplayName("닉네임이 null이면 회원을 생성할 수 없다.")
    @Test
    void createMemberWithNullNickName() {
        assertThatThrownBy(() -> new Member("posty@splearn.app", null, "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("가입 대기(PENDING) 상태의 회원을 가입 완료시킬 수 있다.")
    @Test
    void activate() {
        // given
        var member = new Member("posty@splearn.app", "Posty", "secret");

        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.ACTIVE);
    }

    @DisplayName("가입 대기(PENDING) 상태가 아니면 회원을 가입 완료시킬 수 없다.")
    @Test
    void activateFail() {
        // given
        var member = new Member("posty@splearn.app", "Posty", "secret");
        member.activate();

        // when & then
        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("가입 완료(ACTIVATE) 상태의 회원을 탈퇴시킬 수 있다.")
    @Test
    void deactivate() {
        // given
        var member = new Member("posty@splearn.app", "Posty", "secret");
        member.activate();

        // when
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.DEACTIVATED);
    }

    @DisplayName("가입 대기 상태의 회원을 탈퇴시킬 수 없다.")
    @Test
    void deactivateFailWhenPending() {
        // given
        var member = new Member("posty@splearn.app", "Posty", "secret");

        // when & then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("탈퇴 상태의 회원을 탈퇴시킬 수 없다.")
    @Test
    void deactivateFailWhenDeactivated() {
        // given
        var member = new Member("posty@splearn.app", "Posty", "secret");
        member.activate();
        member.deactivate();

        // when & then
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }
}
