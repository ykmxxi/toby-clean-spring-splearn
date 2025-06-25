package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

    @DisplayName("이메일 형식에 맞지 않으면 생성할 수 없다.")
    @Test
    void invalidEmail() {
        assertThatThrownBy(() -> new Email("invalid email"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이메일은 주소 값으로 동등성을 비교한다.")
    @Test
    void equality() {
        Email email1 = new Email("test@test.com");
        Email email2 = new Email("test@test.com");

        assertThat(email1).isEqualTo(email2);
    }
}
