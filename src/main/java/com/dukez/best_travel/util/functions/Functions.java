package com.dukez.best_travel.util.functions;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.dukez.best_travel.infrastructure.helpers.EmailHelper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor

public class Functions {
    private final EmailHelper emailHelper;

    public void sendEmail(String email, String nameUser, String product) {
        if (Objects.nonNull(email)) {
            this.emailHelper.sendMail(email, nameUser, product);
        }
    }
}
