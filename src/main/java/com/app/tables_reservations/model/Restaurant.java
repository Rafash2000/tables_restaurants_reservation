package com.app.tables_reservations.model;

import com.app.tables_reservations.jsonDeserializer.CustomLocalTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity(name = "Restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String address;
    private String phone;
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime openingTime;
    @JsonDeserialize(using = CustomLocalTimeDeserializer.class)
    private LocalTime closingTime;
}
