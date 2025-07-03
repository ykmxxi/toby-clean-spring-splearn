package tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.MemberFixture.createMemberRegisterRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.MemberStatus;

@Import(SplearnTestConfiguration.class)
@Transactional
//@TestConstructor(autowireMode = AutowireMode.ALL) // JUnit에게 테스트 클래스 생성 때 @Autowired를 사용하도록 알려줘야 한다
@SpringBootTest
record MemberRegisterTest(MemberRegister memberRegister) {

    @DisplayName("@SpringBootTest를 이용한 멤버 등록 테스트.")
    @Test
    void register() {
        Member member = memberRegister.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);
    }

    @DisplayName("중복된 이메일이 존재하면 회원 등록에 실패한다.")
    @Test
    void duplicateEmailFail() {
        memberRegister.register(createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("잘못된 요청이 들어오면 회원 등록에 실패하낟.")
    @Test
    void memberRegisterRequestFail() {
        invalidRequestFail(new MemberRegisterRequest("tobysplearn.app", "toby123", "secret123"));
        invalidRequestFail(new MemberRegisterRequest("toby@splearn.app", "tob", "secret"));
        invalidRequestFail(new MemberRegisterRequest("toby@splearn.app", "tob111111111111111111111", "secret"));
        invalidRequestFail(new MemberRegisterRequest("toby@splearn.app", "toby", "1234567"));
    }

    private void invalidRequestFail(final MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
