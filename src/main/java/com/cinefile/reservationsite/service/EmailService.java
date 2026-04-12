package com.cinefile.reservationsite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Async
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            String verificationUrl = frontendUrl + "/verify?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("MovieTime - Weryfikacja konta");
            message.setText("Witaj w MovieTime!\n\nKliknij poniższy link aby aktywować konto:\n" + verificationUrl + "\n\nPozdrawiamy,\nMovieTime Team");
            message.setFrom("noreply@movietime.pl");

            mailSender.send(message);

            log.info("Verification email sent to {} z linkiem {}", toEmail, verificationUrl);
        } catch (MailException e) {
            log.error("Error sending verification email to {}: {}", toEmail, e.getMessage());
        }
    }
}
