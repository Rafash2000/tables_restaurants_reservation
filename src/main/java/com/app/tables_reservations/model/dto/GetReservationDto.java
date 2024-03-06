package com.app.tables_reservations.model.dto;

import com.app.tables_reservations.model.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;

public record GetReservationDto(Long id, LocalDate date, LocalTime time, int numberOfPeople, Status status, int tableNumber, String email) {
}
