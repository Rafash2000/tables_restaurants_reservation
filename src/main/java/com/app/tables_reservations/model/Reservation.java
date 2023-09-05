package com.app.tables_reservations.model;

import com.app.tables_reservations.jsonDeserializer.CustomLocalTimeDeserializer;
import com.app.tables_reservations.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
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
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    @JsonIgnore
    private Table table;

    public Reservation(LocalDateTime date, int numberOfPeople, Status status, Restaurant restaurant, Customer customer, Table table) {
        this.date = date;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.restaurant = restaurant;
        this.customer = customer;
        this.table = table;
    }
}
