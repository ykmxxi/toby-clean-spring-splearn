package tobyspring.splearn.domain;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {

    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member() {
    }

    public static Member create(final MemberCreateRequest createRequest, final PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = requireNonNull(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(isActive(), "ACTIVATE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(final String password, final PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(final String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(final String password, final PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
