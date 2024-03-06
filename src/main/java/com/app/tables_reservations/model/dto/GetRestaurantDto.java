package com.app.tables_reservations.model.dto;

public record GetRestaurantDto(Long id, String name, String city, String address, String phone, String openingHours) {
}
