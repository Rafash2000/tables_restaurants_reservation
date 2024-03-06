package com.app.tables_reservations.controller;

import com.app.tables_reservations.controller.dto.AuthenticationDto;
import com.app.tables_reservations.model.Employee;
import com.app.tables_reservations.model.dto.CreateEmployeeDto;
import com.app.tables_reservations.service.EmployeeService;
import com.app.tables_reservations.service.ReservationService;
import com.app.tables_reservations.service.RestaurantService;
import com.app.tables_reservations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@CrossOrigin
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody AuthenticationDto authenticationDto) {
        try {
            if (!userService.login(authenticationDto)) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.findByEmail(authenticationDto.email());

            if (!employeeService.existByUserId(user.getId())) {
                return ResponseEntity.notFound().build();
            }

            var employee = employeeService.getByUserId(user.getId());

            return ResponseEntity.ok(employee.getRestaurant().getId());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            if (!employeeService.existsEmployee(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(employeeService.getEmployeeById(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/create")
    public ResponseEntity<Employee> addEmployee(@RequestBody CreateEmployeeDto createEmployeeDto) {
        try {
            if (!(userService.existUser(createEmployeeDto.userId())
                    && restaurantService.existRestaurant(createEmployeeDto.restaurantId()))) {
                return ResponseEntity.notFound().build();
            }

            var employee = new Employee(
                    userService.getUserById(createEmployeeDto.userId()),
                    restaurantService.getRestaurantById(createEmployeeDto.restaurantId())
            );

            return ResponseEntity.ok(employeeService.addEmployee(
                    employee
            ));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        try {
            if (!employeeService.existsEmployee(id)) {
                return ResponseEntity.notFound().build();
            }

            employeeService.deleteEmployee(id);

            return ResponseEntity.ok("Employee with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        try {
            if (!reservationService.existReservation(id)) {
                return ResponseEntity.notFound().build();
            }

            if (!reservationService.canBeDelete(id)) {
                return ResponseEntity.internalServerError().build();
            }

            reservationService.deleteReservation(id);

            return ResponseEntity.ok("Reservation with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


