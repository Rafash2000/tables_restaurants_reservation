package com.app.tables_reservations.config;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.config.ConfigLoader;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public Mailer mailer() {
        ConfigLoader.loadProperties("email-config.properties", true);
        return MailerBuilder
                .withTransportStrategy(TransportStrategy.SMTPS)
                .async()
                .buildMailer();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
