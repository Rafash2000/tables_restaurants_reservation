package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Reservation;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.dto.GetReservationDto;
import com.app.tables_reservations.model.dto.UpdateReservationDto;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.model.enums.Status;
import com.app.tables_reservations.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


import static com.app.tables_reservations.controller.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {
    @Mock
    private ReservationService reservationService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private TableService tableService;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    public void testGetAllReservationsSuccess() {
        when(reservationService.getAllReservations()).thenReturn(RESERVATION_LIST);

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RESERVATION_LIST, response.getBody());
    }

    @Test
    public void testGetAllReservationsDataAccessException() {
        when(reservationService.getAllReservations()).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservations();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetReservationByIdSuccess() {
        var id = 1L;

        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.getReservationById(id)).thenReturn(RESERVATION_1);

        ResponseEntity<Reservation> response = reservationController.getReservationById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RESERVATION_1, response.getBody());
    }

    @Test
    public void testGetReservationByIdNotFound() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(false);

        ResponseEntity<Reservation> response = reservationController.getReservationById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddReservationSuccess() {
        var mockReservation = new Reservation();

        when(tableService.existTable(5L)).thenReturn(true);
        when(userService.existUser(1L)).thenReturn(true);
        when(tableService.getTableById(5L)).thenReturn(TABLE_5);
        when(userService.getUserById(1L)).thenReturn(USER_1);

        when(reservationService.reserveTable(
                eq(TABLE_5),
                eq(TABLE_5.getRestaurant()),
                eq(TABLE_5.getCapacity()),
                eq(USER_1)
        )).thenReturn(mockReservation);

        when(reservationService.addReservation(any(Reservation.class))).thenReturn(mockReservation);

        ResponseEntity<Reservation> response = reservationController.addReservation(CREATE_RESERVATION_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockReservation, response.getBody());
        verify(emailService).send(eq(USER_1.getEmail()), eq("Potwierdzenie rezerwacji"), eq(mockReservation));
    }

    @Test
    public void testAddReservationTableOrUserNotFound() {
        when(tableService.existTable(CREATE_RESERVATION_DTO.tableId())).thenReturn(false);

        ResponseEntity<Reservation> response = reservationController.addReservation(CREATE_RESERVATION_DTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteReservationSuccess() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(true);
        doNothing().when(reservationService).deleteReservation(id);

        ResponseEntity<String> response = reservationController.deleteReservation(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation with ID: " + id + " deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteReservationNotFound() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(false);

        ResponseEntity<String> response = reservationController.deleteReservation(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteReservationDataAccessException() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<String> response = reservationController.deleteReservation(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetReservationsByRestaurantIdSuccess() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(true);
        when(reservationService.getReservationsByRestaurantId(id)).thenReturn(RESERVATION_LIST);

        ResponseEntity<List<Reservation>> response = reservationController.getReservationsByRestaurantId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RESERVATION_LIST, response.getBody());
    }

    @Test
    public void testGetReservationsByRestaurantIdNotFound() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(false);

        ResponseEntity<List<Reservation>> response = reservationController.getReservationsByRestaurantId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetReservationsByRestaurantIdDataAccessException() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<Reservation>> response = reservationController.getReservationsByRestaurantId(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetTodayReservationByRestaurantIdSuccess() {
        var id = 1L;

        when(restaurantService.existRestaurant(id)).thenReturn(true);
        when(reservationService.getReservationsByRestaurantId(id)).thenReturn(RESERVATION_LIST);

        ResponseEntity<List<GetReservationDto>> response = reservationController.getTodayReservationByRestaurantId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTodayReservationByRestaurantIdNotFound() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(false);

        ResponseEntity<List<GetReservationDto>> response = reservationController.getTodayReservationByRestaurantId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetTodayReservationByRestaurantIdDataAccessException() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetReservationDto>> response = reservationController.getTodayReservationByRestaurantId(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetFutureReservationByRestaurantIdSuccess() {
        var id = 1L;

        when(restaurantService.existRestaurant(id)).thenReturn(true);
        when(reservationService.getReservationsByRestaurantId(id)).thenReturn(RESERVATION_LIST);

        ResponseEntity<List<GetReservationDto>> response = reservationController.getFutureReservationByRestaurantId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetFutureReservationByRestaurantIdNotFound() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(false);

        ResponseEntity<List<GetReservationDto>> response = reservationController.getFutureReservationByRestaurantId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFutureReservationByRestaurantIdDataAccessException() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetReservationDto>> response = reservationController.getFutureReservationByRestaurantId(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetPastReservationByRestaurantIdSuccess() {
        var id = 1L;

        when(restaurantService.existRestaurant(id)).thenReturn(true);
        when(reservationService.getReservationsByRestaurantId(id)).thenReturn(RESERVATION_LIST);

        ResponseEntity<List<GetReservationDto>> response = reservationController.getPastReservationByRestaurantId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetPastReservationByRestaurantIdNotFound() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenReturn(false);

        ResponseEntity<List<GetReservationDto>> response = reservationController.getPastReservationByRestaurantId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetPastReservationByRestaurantIdDataAccessException() {
        var id = 1L;
        when(restaurantService.existRestaurant(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<GetReservationDto>> response = reservationController.getPastReservationByRestaurantId(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCancelReservationSuccess() {
        var id = 1L;

        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.getReservationById(id)).thenReturn(RESERVATION_4);
        when(reservationService.save(any(Reservation.class))).thenReturn(RESERVATION_4);
        when(tableService.save(any(Table.class))).thenReturn(TABLE_4);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Status.canceled, response.getBody().getStatus());
        assertEquals(Availability.available, TABLE_4.getAvailability());
    }

    @Test
    public void testCancelReservationNotFound() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(false);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCancelReservationNotPossible() {
        var id = 1L;

        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.getReservationById(id)).thenReturn(RESERVATION_1);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCancelReservationDataAccessException() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateReservationSuccess() {
        var id = 1L;
        var newDateTime = LocalDateTime.of(UPDATE_RESERVATION_DTO.date(), UPDATE_RESERVATION_DTO.time());

        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.getReservationById(id)).thenReturn(RESERVATION_3);
        when(tableService.getTableById(TABLE_3.getId())).thenReturn(TABLE_3);
        when(reservationService.save(any(Reservation.class))).thenReturn(RESERVATION_3);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id, UPDATE_RESERVATION_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newDateTime, RESERVATION_3.getDate());
    }

    @Test
    public void testUpdateReservationNotFound() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(false);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id, UPDATE_RESERVATION_DTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateReservationInvalidDate() {
        var id = 1L;
        var reservationDto = new UpdateReservationDto(LocalDate.now().minusDays(1), LocalTime.of(12, 0));

        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.getReservationById(id)).thenReturn(RESERVATION_4);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id, reservationDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateReservationDataAccessException() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Reservation> response = reservationController.cancelReservation(id, UPDATE_RESERVATION_DTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}