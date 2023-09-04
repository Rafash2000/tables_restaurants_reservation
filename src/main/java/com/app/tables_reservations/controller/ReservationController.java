package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Customer;
import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.service.CustomerService;
import com.app.tables_reservations.service.ReservationService;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    private final RestaurantService restaurantService;

    private final TableService tableService;

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            return ResponseEntity.ok(reservationService.getAllReservations());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        try {
            if (!reservationService.existReservation(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(reservationService.getReservationById(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/addReservation/{restaurantId}/{tableId}/{customerId}")
    public ResponseEntity<Reservation> addReservation(
            @RequestBody Reservation reservation, @PathVariable Long restaurantId,
            @PathVariable Long tableId, @PathVariable Long customerId) {
        try {
            if (!(reservationService.existReservation(restaurantId) || tableService.existTable(tableId)
                || customerService.existCustomer(customerId))) {
                reservation.setCustomer(customerService.getCustomerById(customerId));
                reservation.setRestaurant(restaurantService.getRestaurantById(restaurantId));
                reservation.setTable(tableService.getTableById(tableId));
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(reservationService.addReservation(reservation));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        try {
            if (!reservationService.existReservation(id)) {
                return ResponseEntity.notFound().build();
            }

            reservationService.deleteReservation(id);

            return ResponseEntity.ok("Reservation with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
