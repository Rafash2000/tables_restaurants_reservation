package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final Mailer mailer;

    public void send(String receiver, String subject, String htmlContent) {
        var email = EmailBuilder
                .startingBlank()
                .withSubject(subject)
                .withHTMLText(htmlContent)
                .to(receiver)
                .buildEmail();

        mailer
                .sendMail(email)
                .thenAccept(System.out::println);
    }

    public void send(String receiver, String subject, Long userId) {
        String htmlContent = formatHtmlContent(userId);

        var email = EmailBuilder
                .startingBlank()
                .withSubject(subject)
                .withHTMLText(htmlContent)
                .to(receiver)
                .buildEmail();

        mailer
                .sendMail(email)
                .thenAccept(System.out::println);
    }

    private String formatHtmlContent(Long userId) {
        String activationUrl = "http://localhost:8081/users/active/" + userId;

        return "<html>" +
                "<head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                "a { color: #0066cc; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                "</style></head>" +
                "<body>" +
                "<p>Aktywuj swoje konto klikając w poniższy link:</p>" +
                "<a href='" + activationUrl + "'>" + activationUrl + "</a>" +
                "</body></html>";
    }

    public void send(String receiver, String subject, Reservation reservation) {
        String htmlContent = formatHtmlContent(reservation);

        var email = EmailBuilder
                .startingBlank()
                .withSubject(subject)
                .withHTMLText(htmlContent)
                .to(receiver)
                .buildEmail();

        mailer
                .sendMail(email)
                .thenAccept(System.out::println);
    }

    private String formatHtmlContent(Reservation reservation) {
        return "<html>" +
                "<head><style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                "h2 { color: #333366; }" +
                "p { color: #666666; }" +
                ".content { background-color: #f4f4f4; padding: 20px; }" +
                ".footer { color: #aaaaaa; font-size: 0.8em; }" +
                "</style></head>" +
                "<body>" +
                "<div class='content'>" +
                "<h2>Potwierdzenie Rezerwacji</h2>" +
                "<p>MIEJSCE: " + reservation.getTable().getRestaurant().getName() + "</p>" +
                "<p>ADRES: " + reservation.getTable().getRestaurant().getCity() + ", " +
                reservation.getTable().getRestaurant().getAddress() + "</p>" +
                "<p>DATA: " + reservation.getDate().toLocalDate() + ", " + reservation.getDate().toLocalTime() + "</p>" +
                "<p>LICZBA OSOB: " + reservation.getNumberOfPeople() + "</p>" +
                "<p>NUMER STOLIKA: " + reservation.getTable().getNumber() + "</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Dziękujemy za skorzystanie z naszych usług.</p>" +
                "</div>" +
                "</body></html>";
    }
}
