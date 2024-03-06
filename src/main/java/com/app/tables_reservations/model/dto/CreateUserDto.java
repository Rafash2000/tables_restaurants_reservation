package com.app.tables_reservations.model.dto;

import com.app.tables_reservations.model.User;

public record CreateUserDto(String name, String surname, String email, String password) {
    public User toUser() {
        return new User(name, surname, email, password);
    }
}
