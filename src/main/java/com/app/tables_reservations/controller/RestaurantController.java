package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.dto.CreateRestaurantDto;
import com.app.tables_reservations.model.dto.GetRestaurantDto;
import com.app.tables_reservations.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@CrossOrigin
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<GetRestaurantDto>> getAllRestaurants() {
        try {
            var restaurants = restaurantService
                    .getAllRestaurants()
                    .stream()
                    .map(Restaurant::getRestaurantDto)
                    .toList();
            return ResponseEntity.ok(restaurants);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/new")
    public ResponseEntity<List<GetRestaurantDto>> getNewRestaurants() {
        try {
            var restaurants = restaurantService
                    .getAllRestaurants()
                    .stream()
                    .sorted(Comparator.comparing(Restaurant::getId)
                            .reversed())
                    .limit(5)
                    .map(Restaurant::getRestaurantDto)
                    .toList();
            return ResponseEntity.ok(restaurants);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<GetRestaurantDto>> getRecommendedRestaurants() {
        try {
            var restaurantDto = restaurantService.getRestaurantById(1L).getRestaurantDto();

            var restaurantsTopList = List.of(restaurantDto);

            return ResponseEntity.ok(restaurantsTopList);
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

    @PostMapping("/create")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody CreateRestaurantDto createRestaurantDto) {
        try {
            return ResponseEntity.ok(restaurantService.addRestaurant(createRestaurantDto.toRestaurant()));
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
