package tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import tobyspring.splearn.application.MemberService;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberStatus;

class MemberRegisterManualTest {

    @DisplayName("수동 Stub을 사용한 회원 저장 테스트.")
    @Test
    void registerTestStub() {
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);
    }

    @DisplayName("수동 Mock을 사용한 회원 저장 테스트.")
    @Test
    void registerTestMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos()).hasSize(1);
        assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
    }

    @DisplayName("Mockito 라이브러리를 사용한 회원 저장 테스트.")
    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);

        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualByComparingTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock)
                .send(eq(member.getEmail()), any(), any());
    }

    static class MemberRepositoryStub implements MemberRepository {

        @Override
        public Member save(final Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(final Email email) {
            return Optional.empty();
        }
    }

    static class EmailSenderStub implements EmailSender {

        @Override
        public void send(final Email email, final String subject, final String body) {
        }
    }

    // Mock을 사용할 때 호출 여부 검증, 몇 번 호출했는지, 어떤 파라미터가 넘어왔는지 등 필요한 것을 검증
    static class EmailSenderMock implements EmailSender {

        List<Email> tos = new ArrayList<>();

        @Override
        public void send(final Email email, final String subject, final String body) {
            tos.add(email);
        }

        public List<Email> getTos() {
            return tos;
        }
    }
}
