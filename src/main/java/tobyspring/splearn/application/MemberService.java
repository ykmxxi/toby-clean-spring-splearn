package tobyspring.splearn.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.PasswordEncoder;

@RequiredArgsConstructor
@Service
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(final MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    private void checkDuplicateEmail(final MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email()))
                .isPresent()
        ) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다: " + registerRequest.email());
        }
    }

    private void sendWelcomeEmail(final Member member) {
        emailSender.send(member.getEmail(), "회원 등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }
}
