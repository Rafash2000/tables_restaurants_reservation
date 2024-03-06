package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.dto.CreateReservationDto;
import com.app.tables_reservations.model.dto.GetReservationDto;
import com.app.tables_reservations.model.dto.UpdateReservationDto;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.model.enums.Status;
import com.app.tables_reservations.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@CrossOrigin
public class ReservationController {
    private final ReservationService reservationService;
    private final RestaurantService restaurantService;
    private final TableService tableService;
    private final UserService userService;
    private final EmailService emailService;

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

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> addReservation(@RequestBody CreateReservationDto createReservationDto) {
        try {
            if (!(tableService.existTable(createReservationDto.tableId()) && userService.existUser(createReservationDto.userId()))) {
                return ResponseEntity.notFound().build();
            }

            var table = tableService.getTableById(createReservationDto.tableId());
            var user = userService.getUserById(createReservationDto.userId());
            var restaurant = table.getRestaurant();

            if (!LocalDateTime.of(table.getDate(), table.getTime()).isAfter(LocalDateTime.now())) {
                return ResponseEntity.notFound().build();
            }

            if (table.getAvailability().equals(Availability.reserved)) {
                return ResponseEntity.notFound().build();
            }


            var reservation = reservationService.reserveTable(table, restaurant, table.getCapacity(), user);

            table.setAvailability(Availability.reserved);
            tableService.save(table);
            reservation = reservationService.addReservation(reservation);

            emailService.send(user.getEmail(), "Potwierdzenie rezerwacji", reservation);

            return ResponseEntity.ok(reservation);
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

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByRestaurantId(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(reservationService.getReservationsByRestaurantId(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/today/{id}")
    public ResponseEntity<List<GetReservationDto>> getTodayReservationByRestaurantId(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            var reservations = reservationService.getReservationsByRestaurantId(id);

            var todayReservation = reservations
                    .stream()
                    .filter(reservation -> reservation.getDate().toLocalDate().isEqual(LocalDate.now()))
                    .filter(reservation -> reservation.getDate().isAfter(LocalDateTime.now()))
                    .filter(reservation -> reservation.getStatus().equals(Status.confirmed))
                    .map(Reservation::getReservationDto)
                    .toList();

            return ResponseEntity.ok(todayReservation);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/future/{id}")
    public ResponseEntity<List<GetReservationDto>> getFutureReservationByRestaurantId(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            var reservations = reservationService.getReservationsByRestaurantId(id);

            var futureReservation = reservations
                    .stream()
                    .filter(reservation -> reservation.getDate().toLocalDate().isAfter(LocalDate.now()))
                    .filter(reservation -> reservation.getStatus().equals(Status.confirmed))
                    .map(Reservation::getReservationDto)
                    .toList();

            return ResponseEntity.ok(futureReservation);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/past/{id}")
    public ResponseEntity<List<GetReservationDto>> getPastReservationByRestaurantId(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            var reservations = reservationService.getReservationsByRestaurantId(id);

            var pastReservation = reservations
                    .stream()
                    .filter(reservation -> reservation.getDate().isBefore(LocalDateTime.now()))
                    .filter(reservation -> reservation.getStatus().equals(Status.confirmed))
                    .map(Reservation::getReservationDto)
                    .toList();

            return ResponseEntity.ok(pastReservation);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        try {
            if (!reservationService.existReservation(id)) {
                return ResponseEntity.notFound().build();
            }

            var reservation = reservationService.getReservationById(id);


            if (!reservation.getDate().isAfter(LocalDateTime.now())) {
                return ResponseEntity.internalServerError().build();
            }

            reservation.setStatus(Status.canceled);

            var table = reservation.getTable();
            table.setAvailability(Availability.available);

            tableService.save(table);

            return ResponseEntity.ok(reservationService.save(reservation));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id, @RequestBody UpdateReservationDto reservationDto) {
        try {
            if (!reservationService.existReservation(id)) {
                return ResponseEntity.notFound().build();
            }

            var reservation = reservationService.getReservationById(id);

            var date = reservationDto.date();
            var time = reservationDto.time();

            var dateTime = LocalDateTime.of(date, time);


            if (!dateTime.isAfter(LocalDateTime.now())) {
                return ResponseEntity.internalServerError().build();
            }

            if (!reservation.getDate().isAfter(LocalDateTime.now().plusHours(24))) {
                return ResponseEntity.notFound().build();
            }

            reservation.setDate(dateTime);

            var tableId = reservation.getTable().getId();

            var table = tableService.getTableById(tableId);
            var tableNumber = table.getNumber();
            var tableCapacity = table.getCapacity();

            var restaurant = reservation.getRestaurant();

            table.setAvailability(Availability.available);
            table = tableService.save(table);

            var newTable = tableService.addTable(new Table(tableNumber, tableCapacity, date, time, Availability.reserved, restaurant));

            return ResponseEntity.ok(reservationService.save(reservation));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
