package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Employee;
import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.User;
import com.app.tables_reservations.model.dto.*;
import com.app.tables_reservations.model.enums.Role;
import com.app.tables_reservations.service.EmployeeService;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Objects;

import static com.app.tables_reservations.controller.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void testLoginSuccess() {
        when(userService.login(AUTHENTICATION_DTO_ADMIN)).thenReturn(true);
        when(userService.findByEmail("email11@email.com")).thenReturn(ADMIN_1);

        ResponseEntity<User> response = adminController.login(AUTHENTICATION_DTO_ADMIN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Role.admin, Objects.requireNonNull(response.getBody()).getRole());
    }

    @Test
    public void testLoginFailure() {
        when(userService.login(AUTHENTICATION_DTO_ADMIN)).thenReturn(false);

        ResponseEntity<User> response = adminController.login(AUTHENTICATION_DTO_ADMIN);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddAdminSuccess() {
        when(bCryptPasswordEncoder.encode(ADMIN_1.getPassword())).thenReturn("password");
        when(userService.addUser(any(User.class))).thenReturn(ADMIN_1);

        ResponseEntity<User> response = adminController.addAdmin(CREATE_ADMIN_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Role.admin, response.getBody().getRole());
        assertTrue(response.getBody().isEnabled());
        assertEquals("password", response.getBody().getPassword());
    }

    @Test
    void testAddAdminDataAccessException() {
        when(bCryptPasswordEncoder.encode(ADMIN_1.getPassword())).thenReturn("password");
        when(userService.addUser(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        ResponseEntity<User> response = adminController.addAdmin(CREATE_ADMIN_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addRestaurantShouldReturnRestaurantWhenSuccessful() {

        when(restaurantService.addRestaurant(any(Restaurant.class))).thenReturn(RESTAURANT_1);

        ResponseEntity<Restaurant> response = adminController.addRestaurant(CREATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RESTAURANT_1, response.getBody());
    }

    @Test
    void addRestaurantShouldReturnInternalServerErrorOnException() {
        when(restaurantService.addRestaurant(any(Restaurant.class))).thenThrow(new EmptyResultDataAccessException(1));

        ResponseEntity<Restaurant> response = adminController.addRestaurant(CREATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getAllRestaurantsShouldReturnListWhenNoError() {
        when(restaurantService.getAllRestaurants()).thenReturn(RESTAURANT_LIST);

        ResponseEntity<List<Restaurant>> response = adminController.getAllRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RESTAURANT_LIST.size(), response.getBody().size());
    }

    @Test
    void getAllRestaurantsShouldReturnInternalServerErrorOnException() {
        when(restaurantService.getAllRestaurants()).thenThrow(new DataAccessException("Database error") {});

        ResponseEntity<List<Restaurant>> response = adminController.getAllRestaurants();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateRestaurantShouldReturnUpdatedRestaurant() {
        var restaurantId = 1L;

        when(restaurantService.existRestaurant(restaurantId)).thenReturn(true);
        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(RESTAURANT_1);
        when(restaurantService.updateRestaurant(RESTAURANT_1)).thenReturn(RESTAURANT_1);

        ResponseEntity<Restaurant> response = adminController.updateRestaurant(restaurantId, UPDATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RESTAURANT_1, response.getBody());
    }

    @Test
    void updateRestaurantShouldReturnNotFoundWhenRestaurantDoesNotExist() {
        var restaurantId = 1L;

        when(restaurantService.existRestaurant(restaurantId)).thenReturn(false);

        ResponseEntity<Restaurant> response = adminController.updateRestaurant(restaurantId, UPDATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateRestaurantShouldReturnInternalServerErrorOnException() {
        var restaurantId = 1L;

        when(restaurantService.existRestaurant(restaurantId)).thenReturn(true);
        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(new Restaurant());
        when(restaurantService.updateRestaurant(any())).thenThrow(new DataAccessException("Database error") {});

        ResponseEntity<Restaurant> response = adminController.updateRestaurant(restaurantId, UPDATE_RESTAURANT_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void disableRestaurantShouldDisableAndReturnUpdatedRestaurant() {
        var restaurantId = 1L;

        when(restaurantService.existRestaurant(restaurantId)).thenReturn(true);
        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(RESTAURANT_1);
        when(restaurantService.updateRestaurant(RESTAURANT_1)).thenReturn(RESTAURANT_1);

        ResponseEntity<Restaurant> response = adminController.disableRestaurant(restaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAvailable());
    }

    @Test
    void disableRestaurantShouldReturnNotFoundWhenRestaurantDoesNotExist() {
        var restaurantId = 1L;
        when(restaurantService.existRestaurant(restaurantId)).thenReturn(false);

        ResponseEntity<Restaurant> response = adminController.disableRestaurant(restaurantId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void disableRestaurantShouldReturnInternalServerErrorOnException() {
        var restaurantId = 1L;
        when(restaurantService.existRestaurant(restaurantId)).thenReturn(true);
        when(restaurantService.getRestaurantById(restaurantId)).thenReturn(new Restaurant());
        when(restaurantService.updateRestaurant(any())).thenThrow(new DataAccessException("Database error") {});

        ResponseEntity<Restaurant> response = adminController.disableRestaurant(restaurantId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addEmployeeShouldAddAndReturnEmployee() {

        when(userService.existUserByEmail(CREATE_EMPLOYEE_1.email())).thenReturn(true);
        when(userService.findByEmail(CREATE_EMPLOYEE_1.email())).thenReturn(USER_1);
        when(restaurantService.getRestaurantById(CREATE_EMPLOYEE_1.restaurantId())).thenReturn(RESTAURANT_1);
        when(employeeService.addEmployee(any())).thenReturn(new Employee(USER_1, RESTAURANT_1));

        ResponseEntity<Employee> response = adminController.addEmployee(CREATE_EMPLOYEE_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void addEmployeeShouldReturnNotFoundWhenUserDoesNotExist() {
        when(userService.existUserByEmail(CREATE_EMPLOYEE_1.email())).thenReturn(false);

        ResponseEntity<Employee> response = adminController.addEmployee(CREATE_EMPLOYEE_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addEmployeeShouldReturnInternalServerErrorOnException() {

        when(userService.existUserByEmail(CREATE_EMPLOYEE_1.email())).thenReturn(true);
        when(userService.findByEmail(CREATE_EMPLOYEE_1.email())).thenReturn(new User());
        when(restaurantService.getRestaurantById(CREATE_EMPLOYEE_1.restaurantId())).thenReturn(new Restaurant());
        when(employeeService.addEmployee(any())).thenThrow(new DataAccessException("Database error") {});

        ResponseEntity<Employee> response = adminController.addEmployee(CREATE_EMPLOYEE_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteUserShouldDisableUserAndReturnSuccessMessage() {
        var userEmail = USER_1.getEmail();
        var deleteUserDto = new DeleteUserDto(userEmail);


        when(userService.existUserByEmail(userEmail)).thenReturn(true);
        when(userService.findByEmail(userEmail)).thenReturn(USER_1);

        ResponseEntity<String> response = adminController.deleteUser(deleteUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with ID: %d deleted successfully".formatted(USER_1.getId()), response.getBody());
        assertFalse(USER_1.isEnabled());
        verify(userService, times(1)).updateUser(USER_1);
    }

    @Test
    void deleteUserShouldReturnNotFoundWhenUserDoesNotExist() {
        var userEmail = USER_1.getEmail();
        var deleteUserDto = new DeleteUserDto(userEmail);

        when(userService.existUserByEmail(userEmail)).thenReturn(false);

        ResponseEntity<String> response = adminController.deleteUser(deleteUserDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteUserShouldReturnInternalServerErrorOnException() {
        var userEmail = USER_1.getEmail();
        var deleteUserDto = new DeleteUserDto(userEmail);

        when(userService.existUserByEmail(userEmail)).thenReturn(true);
        when(userService.findByEmail(userEmail)).thenReturn(new User());
        doThrow(new DataAccessException("Database error") {}).when(userService).updateUser(any());

        ResponseEntity<String> response = adminController.deleteUser(deleteUserDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}