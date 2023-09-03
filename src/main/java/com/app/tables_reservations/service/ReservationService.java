package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
