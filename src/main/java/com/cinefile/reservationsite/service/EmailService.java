package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.model.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

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

    @Async
    public void sendTicketConfirmation(String toEmail, List<Ticket> tickets) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("MovieTime - Bilety");
            helper.setFrom("noreply@movietime.pl");

            StringBuilder content = new StringBuilder("Twoje bilety:\n\n");

            int i = 1;

            for (Ticket ticket : tickets) {

                String qrText = "Film: " + ticket.getScreening().getMovie().getTitle() +
                        "\nRząd: " + ticket.getRow() +
                        "\nMiejsce: " + ticket.getCol() +
                        "\nData: " + ticket.getScreening().getStartTime();

                byte[] qrCode = generateQrCode(qrText);

                helper.addAttachment("ticket-" + i + ".png",
                        new ByteArrayResource(qrCode));

                content.append("Bilet ").append(i)
                        .append(": rząd ")
                        .append(ticket.getRow())
                        .append(" miejsce ")
                        .append(ticket.getCol())
                        .append("\n");

                i++;
            }

            helper.setText(content.toString());

            mailSender.send(message);

        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

    private byte[] generateQrCode(String text) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

        return baos.toByteArray();
    }
}
