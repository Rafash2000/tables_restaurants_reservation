package com.app.tables_reservations.model;

import com.app.tables_reservations.jsonDeserializer.CustomLocalTimeDeserializer;
import com.app.tables_reservations.model.dto.GetTableDto;
import com.app.tables_reservations.model.enums.Availability;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "restaurant_tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int capacity;
    private LocalDate date;
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime time;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private Restaurant restaurant;

    public GetTableDto toGetTableDto(){
        return new GetTableDto(id, number, capacity, date, time.toString(), restaurant.getId());
    }

    public Table(int number, int capacity, LocalDate date, LocalTime time, Availability availability, Restaurant restaurant) {
        this.number = number;
        this.capacity = capacity;
        this.date = date;
        this.time = time;
        this.availability = availability;
        this.restaurant = restaurant;
    }
}
