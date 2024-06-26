package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.User;
import com.app.tables_reservations.model.enums.Status;
import com.app.tables_reservations.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() { return reservationRepository.findAll(); }

    public Reservation getReservationById(Long id) { return reservationRepository.findById(id).orElse(null); }

    public Reservation addReservation(Reservation reservation) { return reservationRepository.save(reservation); }

    public boolean existReservation(Long id) { return reservationRepository.existsById(id); }

    public void deleteReservation(Long id) { reservationRepository.deleteById(id); }

    public Reservation reserveTable(Table table, Restaurant restaurant, int numberOfPeople, User user) {
        return new Reservation(
                table.getDate().atTime(table.getTime()), numberOfPeople, Status.confirmed, restaurant, table, user);
    }

    public List<Reservation> getReservationsByRestaurantId(Long id) { return reservationRepository.getReservationsByRestaurantId(id); }

    public boolean canBeDelete(Long id) {
        var reservation = reservationRepository.findById(id).orElseThrow();
        var date = reservation.getDate().toLocalDate();

        return date.isAfter(LocalDate.now());
    }

    public Reservation save(Reservation reservation) { return reservationRepository.save(reservation); }
}
