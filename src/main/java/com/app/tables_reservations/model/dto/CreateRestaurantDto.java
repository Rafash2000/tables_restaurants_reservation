package com.app.tables_reservations.model.dto;

import com.app.tables_reservations.model.Restaurant;

import java.time.LocalTime;

public record CreateRestaurantDto(String name, String city, String address, String phone,
                                  LocalTime openingTime, LocalTime closingTime) {
    public Restaurant toRestaurant() {
        return new Restaurant(name, city, address, phone, openingTime, closingTime);
    }
}
