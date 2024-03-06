package com.app.tables_reservations.model.dto;

import com.app.tables_reservations.model.Restaurant;

import java.time.LocalTime;

public record UpdateRestaurantDto(String name, String city, String address, String phone, LocalTime openingTime, LocalTime closingTime) {
    public Restaurant toRestaurant(Long id) {
        return new Restaurant(id, city, address, phone, openingTime, closingTime);
    }
}
