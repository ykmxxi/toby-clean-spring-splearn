package tobyspring.splearn.domain;

public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest(final String email) {
        return new MemberRegisterRequest(email, "Posty", "secret");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("posty@splearn.app");
    }
    
    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(final String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(final String password, final String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}
