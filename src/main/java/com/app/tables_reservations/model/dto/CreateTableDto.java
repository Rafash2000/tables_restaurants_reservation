package com.app.tables_reservations.model.dto;

import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.enums.Availability;

import java.time.LocalDate;
import java.time.LocalTime;


public record CreateTableDto(int number, int capacity, LocalDate date, LocalTime time, Availability availability, Long restaurantId) {
    public Table toTable(Restaurant restaurant) {
        return new Table(number, capacity, date, time, availability, restaurant);
    }
}
