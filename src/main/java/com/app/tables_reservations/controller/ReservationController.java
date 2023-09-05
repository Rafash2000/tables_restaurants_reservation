package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Customer;
import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.model.enums.Status;
import com.app.tables_reservations.service.CustomerService;
import com.app.tables_reservations.service.ReservationService;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
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

    @PostMapping("/addReservation/{restaurantId}/{customerId}/{numberOfPeople}/{date}")
    public ResponseEntity<Reservation> addReservation(
            @PathVariable Long restaurantId,
            @PathVariable Long customerId,
            @PathVariable int numberOfPeople,
            @PathVariable LocalDateTime date) {
        try {
            if (!(reservationService.existReservation(restaurantId)
                    || customerService.existCustomer(customerId))) {
                return ResponseEntity.notFound().build();
            }

            var freeTables = tableService.findFreeTable(restaurantId, date, numberOfPeople);

            if (freeTables.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var table = freeTables.get(0);

            var reservation = reservationService.reserveTable(
                    table,
                    restaurantService.getRestaurantById(restaurantId),
                    customerService.getCustomerById(customerId),
                    numberOfPeople);

            table.setAvailability(Availability.reserved);
            tableService.save(table);
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
