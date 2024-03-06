package com.app.tables_reservations.controller;

import com.app.tables_reservations.controller.dto.AuthenticationDto;
import com.app.tables_reservations.model.Employee;
import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.User;
import com.app.tables_reservations.model.dto.*;
import com.app.tables_reservations.model.enums.Role;
import com.app.tables_reservations.service.EmployeeService;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {
    private final UserService userService;
    private final EmployeeService employeeService;
    private final RestaurantService restaurantService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody AuthenticationDto authenticationDto) {
        try {
            if (!userService.login(authenticationDto)) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.findByEmail(authenticationDto.email());

            if (user.getRole().equals(Role.user)) {
                return ResponseEntity.internalServerError().build();
            }

            return ResponseEntity.ok(user);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> addAdmin(@RequestBody CreateUserDto createUserDto) {
        try {
            var user = createUserDto.toUser();

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRole(Role.admin);
            user.setEnabled(true);

            return ResponseEntity.ok(userService.addUser(user));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/restaurants/create")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody CreateRestaurantDto createRestaurantDto) {
        try {
            var restaurant  = createRestaurantDto.toRestaurant();
            restaurant.setAvailable(true);
            return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        try {
            return ResponseEntity.ok(restaurantService.getAllRestaurants());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody UpdateRestaurantDto restaurantDto) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            var restaurant = restaurantService.getRestaurantById(id);

            restaurant.update(restaurantDto);

            return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/restaurants/disable/{id}")
    public ResponseEntity<Restaurant> disableRestaurant(@PathVariable Long id) {
        try {
            if (!restaurantService.existRestaurant(id)) {
                return ResponseEntity.notFound().build();
            }

            var restaurant = restaurantService.getRestaurantById(id);

            restaurant.setAvailable(false);

            return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody CreateEmployee createEmployee) {
        try {
            if (!userService.existUserByEmail(createEmployee.email())) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.findByEmail(createEmployee.email());
            var restaurant = restaurantService.getRestaurantById(createEmployee.restaurantId());

            return ResponseEntity.ok(employeeService.addEmployee(new Employee(user, restaurant)));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserDto deleteUserDto) {
        try {
            var email = deleteUserDto.email();

            if (!userService.existUserByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.findByEmail(email);

            user.setEnabled(false);

            userService.updateUser(user);

            return ResponseEntity.ok("User with ID: %d deleted successfully".formatted(user.getId()));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
