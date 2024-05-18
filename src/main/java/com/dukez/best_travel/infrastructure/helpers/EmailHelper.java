package com.dukez.best_travel.infrastructure.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class EmailHelper {
    private final JavaMailSender mailSender;

    public void sendMail(String to, String name, String product) {
        MimeMessage message = mailSender.createMimeMessage();
        String htmlContent = readHTMLTemplate(name, product);

        try {
            message.setFrom(new InternetAddress("duketryprog@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject("Test email from my Springapplication");

            message.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sending email: " + e.getMessage());
        }
    }

    private String readHTMLTemplate(String name, String product) {
        try (var lines = Files.lines(TEMPLATE_PATH)) {
            var html = lines.collect(Collectors.joining());
            return html.replace("{name}", name).replace("{product}", product);
        } catch (IOException e) {
            log.error("Cant read html template", e);
            throw new RuntimeException();
        }
    }

    private final Path TEMPLATE_PATH = Path.of("src/main/resources/email/email_template.html");
}
