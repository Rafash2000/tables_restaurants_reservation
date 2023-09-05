package com.app.tables_reservations.model;

import com.app.tables_reservations.jsonDeserializer.CustomLocalTimeDeserializer;
import com.app.tables_reservations.model.enums.Availability;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity(name = "Tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int capacity;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;
}
