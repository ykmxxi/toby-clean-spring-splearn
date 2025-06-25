package tobyspring.splearn.domain;

import static org.springframework.util.Assert.state;

import java.util.Objects;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {

    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member(final String email, final String nickname, final String passwordHash) {
        this.email = Objects.requireNonNull(email);
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    public static Member create(final String email, final String nickname, final String password,
                                final PasswordEncoder passwordEncoder) {
        return new Member(email, nickname, passwordEncoder.encode(password));
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVATE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(final String password, final PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(final String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changePassword(final String password, final PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }
}
