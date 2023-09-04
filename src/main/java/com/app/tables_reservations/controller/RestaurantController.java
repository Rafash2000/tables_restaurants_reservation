package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Customer;
import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        try {
            return ResponseEntity.ok(restaurantService.getAllRestaurants());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(restaurantService.getRestaurantById(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/addRestaurant")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        try {
            System.out.println(restaurant.getOpeningTime());
            restaurant.setOpeningTime(restaurant.getOpeningTime().truncatedTo(ChronoUnit.MINUTES));
            restaurant.setClosingTime(restaurant.getClosingTime().truncatedTo(ChronoUnit.MINUTES));
            return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            restaurantService.deleteRestaurant(id);

            return ResponseEntity.ok("Restaurant with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
