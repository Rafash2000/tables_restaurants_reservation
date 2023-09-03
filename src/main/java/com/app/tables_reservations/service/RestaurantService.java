package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.repository.ReservationRepository;
import com.app.tables_reservations.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllReservations() { return restaurantRepository.findAll(); }

    public Restaurant getCustomerById(Long id) { return restaurantRepository.findById(id).orElse(null); }

    public Restaurant addRestaurant(Restaurant restaurant) { return restaurantRepository.save(restaurant); }

    public boolean existRestaurant(Long id) { return restaurantRepository.existsById(id); }

    public void deleteRestaurant(Long id) { restaurantRepository.deleteById(id); }
}
