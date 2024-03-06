package com.app.tables_reservations.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationDto(LocalDate date, LocalTime time) {
}
