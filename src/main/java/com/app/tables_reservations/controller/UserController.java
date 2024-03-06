package com.app.tables_reservations.controller;

import com.app.tables_reservations.controller.dto.AuthenticationDto;
import com.app.tables_reservations.model.User;
import com.app.tables_reservations.model.dto.CreateUserDto;
//import com.app.tables_reservations.service.AuthenticationService;
import com.app.tables_reservations.model.dto.DeleteUserDto;
import com.app.tables_reservations.service.EmailService;
import com.app.tables_reservations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            if (!userService.existUser(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody CreateUserDto createUserDto) {
        try {
            var user = createUserDto.toUser();

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            userService.addUser(user);

            emailService.send(
                    user.getEmail(),
                    "Activate your account",
                    user.getId()
            );
            return ResponseEntity.ok(user);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody AuthenticationDto authenticationDto) {
        try {
            if (!userService.login(authenticationDto)) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.findByEmail(authenticationDto.email());

            return ResponseEntity.ok(user);
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            if (!userService.existUser(id)) {
                return ResponseEntity.notFound().build();
            }

            userService.deleteUser(id);

            return ResponseEntity.ok("User with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getNameById(@PathVariable Long id) {
        try {
            if (!userService.existUser(id)) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(userService.getUserById(id).getName());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<String> activeAccount(@PathVariable Long id) {
        try {
            if (!userService.existUser(id)) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.getUserById(id);

            user.activeAccount();

            userService.save(user);

            return ResponseEntity.ok("Konto zostało aktywowane");
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/disable")
    public ResponseEntity<User> disableAccount(@RequestBody DeleteUserDto deleteUserDto) {
        try {
            var email = deleteUserDto.email();

            if (!userService.existUserByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            var user = userService.findByEmail(email);

            user.setEnabled(false);

            return ResponseEntity.ok(userService.updateUser(user));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
