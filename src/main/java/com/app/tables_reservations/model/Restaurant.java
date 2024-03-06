package com.app.tables_reservations.model;

import com.app.tables_reservations.jsonDeserializer.CustomLocalTimeDeserializer;
import com.app.tables_reservations.model.dto.GetRestaurantDto;
import com.app.tables_reservations.model.dto.UpdateRestaurantDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
    private boolean available;

    public Restaurant(Long id, String city, String address, String phone, LocalTime openingTime, LocalTime closingTime) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Restaurant(String name, String city, String address, String phone, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public GetRestaurantDto getRestaurantDto() {
        return new GetRestaurantDto(id, name, city, address, phone, String.format(openingTime.toString()+ " - " + closingTime.toString()));
    }

    public void update(UpdateRestaurantDto restaurantDto) {
        this.name = restaurantDto.name();
        this.city = restaurantDto.city();
        this.address = restaurantDto.address();
        this.phone = restaurantDto.phone();
        this.openingTime = restaurantDto.openingTime();
        this.closingTime = restaurantDto.closingTime();
    }
}
