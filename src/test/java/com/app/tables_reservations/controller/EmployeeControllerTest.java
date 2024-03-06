package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Employee;
import com.app.tables_reservations.service.EmployeeService;
import com.app.tables_reservations.service.ReservationService;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.app.tables_reservations.controller.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testGetAllEmployeesSuccess() {
        when(employeeService.getAllEmployees()).thenReturn(EMPLOYEE_LIST);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EMPLOYEE_LIST, response.getBody());
    }

    @Test
    public void testGetAllEmployeesDataAccessException() {
        when(employeeService.getAllEmployees()).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLoginSuccess() {
        when(userService.login(AUTHENTICATION_DTO_1)).thenReturn(true);
        when(userService.findByEmail(AUTHENTICATION_DTO_1.email())).thenReturn(USER_1);
        when(employeeService.existByUserId(USER_1.getId())).thenReturn(true);
        when(employeeService.getByUserId(USER_1.getId())).thenReturn(EMPLOYEE_1);

        ResponseEntity<Long> response = employeeController.login(AUTHENTICATION_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody());
    }

    @Test
    public void testLoginUserNotFound() {
        when(userService.login(AUTHENTICATION_DTO_1)).thenReturn(false);

        ResponseEntity<Long> response = employeeController.login(AUTHENTICATION_DTO_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoginEmployeeNotFound() {
        when(userService.login(AUTHENTICATION_DTO_1)).thenReturn(true);
        when(userService.findByEmail(AUTHENTICATION_DTO_1.email())).thenReturn(USER_1);
        when(employeeService.existByUserId(USER_1.getId())).thenReturn(false);

        ResponseEntity<Long> response = employeeController.login(AUTHENTICATION_DTO_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoginDataAccessException() {
        when(userService.login(AUTHENTICATION_DTO_1)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Long> response = employeeController.login(AUTHENTICATION_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetEmployeeByIdSuccess() {
        var id = 1L;

        when(employeeService.existsEmployee(id)).thenReturn(true);
        when(employeeService.getEmployeeById(id)).thenReturn(EMPLOYEE_1);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EMPLOYEE_1, response.getBody());
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        var id = 1L;
        when(employeeService.existsEmployee(id)).thenReturn(false);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetEmployeeByIdDataAccessException() {
        var id = 1L;
        when(employeeService.existsEmployee(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Employee> response = employeeController.getEmployeeById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddEmployeeSuccess() {
        when(userService.existUser(CREATE_EMPLOYEE_DTO_1.userId())).thenReturn(true);
        when(restaurantService.existRestaurant(CREATE_EMPLOYEE_DTO_1.restaurantId())).thenReturn(true);
        when(userService.getUserById(CREATE_EMPLOYEE_DTO_1.userId())).thenReturn(USER_1);
        when(restaurantService.getRestaurantById(CREATE_EMPLOYEE_DTO_1.restaurantId())).thenReturn(RESTAURANT_1);
        when(employeeService.addEmployee(any(Employee.class))).thenReturn(EMPLOYEE_1);

        ResponseEntity<Employee> response = employeeController.addEmployee(CREATE_EMPLOYEE_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddEmployeeUserOrRestaurantNotFound() {
        when(userService.existUser(CREATE_EMPLOYEE_DTO_1.userId())).thenReturn(false);

        ResponseEntity<Employee> response = employeeController.addEmployee(CREATE_EMPLOYEE_DTO_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddEmployeeDataAccessException() {
        when(userService.existUser(CREATE_EMPLOYEE_DTO_1.userId())).thenReturn(true);
        when(restaurantService.existRestaurant(CREATE_EMPLOYEE_DTO_1.restaurantId())).thenReturn(true);
        when(userService.getUserById(CREATE_EMPLOYEE_DTO_1.userId())).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<Employee> response = employeeController.addEmployee(CREATE_EMPLOYEE_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteEmployeeSuccess() {
        var id = 1L;
        when(employeeService.existsEmployee(id)).thenReturn(true);

        ResponseEntity<String> response = employeeController.deleteEmployee(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee with ID: 1 deleted successfully", response.getBody());
        verify(employeeService).deleteEmployee(id);
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        var id = 1L;
        when(employeeService.existsEmployee(id)).thenReturn(false);

        ResponseEntity<String> response = employeeController.deleteEmployee(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeService, never()).deleteEmployee(id);
    }

    @Test
    public void testDeleteEmployeeDataAccessException() {
        var id = 1L;
        when(employeeService.existsEmployee(id)).thenReturn(true);
        doThrow(new DataAccessException("Database Error") {}).when(employeeService).deleteEmployee(id);

        ResponseEntity<String> response = employeeController.deleteEmployee(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteReservationSuccess() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.canBeDelete(id)).thenReturn(true);

        ResponseEntity<String> response = employeeController.deleteReservation(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation with ID: 1 deleted successfully", response.getBody());
        verify(reservationService).deleteReservation(id);
    }

    @Test
    public void testDeleteReservationNotFound() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(false);

        ResponseEntity<String> response = employeeController.deleteReservation(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, never()).deleteReservation(id);
    }

    @Test
    public void testDeleteReservationCannotBeDeleted() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.canBeDelete(id)).thenReturn(false);

        ResponseEntity<String> response = employeeController.deleteReservation(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteReservationDataAccessException() {
        var id = 1L;
        when(reservationService.existReservation(id)).thenReturn(true);
        when(reservationService.canBeDelete(id)).thenReturn(true);
        doThrow(new DataAccessException("Database Error") {}).when(reservationService).deleteReservation(id);

        ResponseEntity<String> response = employeeController.deleteReservation(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}