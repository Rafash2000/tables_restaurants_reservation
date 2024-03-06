package com.app.tables_reservations.model;

import com.app.tables_reservations.model.dto.GetReservationDto;
import com.app.tables_reservations.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private int numberOfPeople;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    @JsonIgnore
    private Table table;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Reservation(LocalDateTime date, int numberOfPeople, Status status, Restaurant restaurant, Table table, User user) {
        this.date = date;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.restaurant = restaurant;
        this.table = table;
        this.user = user;
    }

    public GetReservationDto getReservationDto(){
        return new GetReservationDto(id, date.toLocalDate(), date.toLocalTime(), numberOfPeople, status, table.getNumber(), user.getEmail());
    }
}
