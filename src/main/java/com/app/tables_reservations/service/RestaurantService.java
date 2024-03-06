package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .filter(Restaurant::isAvailable)
                .toList();
    }

    public Restaurant getRestaurantById(Long id) { return restaurantRepository.findById(id).orElse(null); }

    public Restaurant addRestaurant(Restaurant restaurant) { return restaurantRepository.save(restaurant); }

    public boolean existRestaurant(Long id) { return restaurantRepository.existsById(id); }

    public void deleteRestaurant(Long id) { restaurantRepository.deleteById(id); }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}
