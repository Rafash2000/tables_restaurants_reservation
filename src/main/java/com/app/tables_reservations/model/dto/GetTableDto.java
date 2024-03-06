package com.app.tables_reservations.model.dto;

import java.time.LocalDate;

public record GetTableDto(Long id, int number, int capacity, LocalDate date, String time, Long restaurantId) {

}
