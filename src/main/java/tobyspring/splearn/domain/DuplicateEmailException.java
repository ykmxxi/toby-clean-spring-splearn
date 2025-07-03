package tobyspring.splearn.domain;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(final String message) {
        super(message);
    }
}
