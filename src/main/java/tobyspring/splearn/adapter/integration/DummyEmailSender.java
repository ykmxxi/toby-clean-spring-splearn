package tobyspring.splearn.adapter.integration;

import org.springframework.stereotype.Component;

import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Email;

@Component
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(final Email email, final String subject, final String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }
}
