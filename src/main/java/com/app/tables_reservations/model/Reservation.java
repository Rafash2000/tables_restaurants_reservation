package com.app.tables_reservations.model;

import com.app.tables_reservations.jsonDeserializer.CustomLocalTimeDeserializer;
import com.app.tables_reservations.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime startTime;
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime endTime;
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
}
