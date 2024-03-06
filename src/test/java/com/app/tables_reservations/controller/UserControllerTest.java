package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.User;
import com.app.tables_reservations.service.EmailService;
import com.app.tables_reservations.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static com.app.tables_reservations.controller.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsersSuccess() {
        when(userService.getAllUsers()).thenReturn(USER_LIST);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USER_LIST, response.getBody());
    }

    @Test
    public void testGetAllUsersDataAccessException() {
        when(userService.getAllUsers()).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUserByIdSuccess() {
        var id = 1L;

        when(userService.existUser(id)).thenReturn(true);
        when(userService.getUserById(id)).thenReturn(USER_1);

        ResponseEntity<User> response = userController.getUserById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USER_1, response.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() {
        var id = 1L;
        when(userService.existUser(id)).thenReturn(false);

        ResponseEntity<User> response = userController.getUserById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserByIdDataAccessException() {
        var id = 1L;
        when(userService.existUser(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<User> response = userController.getUserById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddUserSuccess() {
        var user = CREATE_USER_DTO_1.toUser();

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("password");
        when(userService.addUser(any(User.class))).thenReturn(user);

        doNothing().when(emailService).send(user.getEmail(), "Activate your account", user.getId());

        ResponseEntity<User> response = userController.addUser(CREATE_USER_DTO_1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("password", response.getBody().getPassword());
        verify(emailService).send(eq(user.getEmail()), eq("Activate your account"), eq(user.getId()));
    }

    @Test
    public void testAddUserDataAccessException() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userService.addUser(any(User.class))).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<User> response = userController.addUser(CREATE_USER_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }



    @Test
    public void testLoginFailed() {
        when(userService.login(AUTHENTICATION_DTO_1)).thenReturn(false);

        ResponseEntity<User> response = userController.login(AUTHENTICATION_DTO_1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoginDataAccessException() {
        when(userService.login(AUTHENTICATION_DTO_1)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<User> response = userController.login(AUTHENTICATION_DTO_1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUserSuccess() {
        var id = 1L;
        when(userService.existUser(id)).thenReturn(true);

        ResponseEntity<String> response = userController.deleteUser(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with ID: 1 deleted successfully", response.getBody());
        verify(userService).deleteUser(id);
    }

    @Test
    public void testDeleteUserNotFound() {
        var id = 1L;
        when(userService.existUser(id)).thenReturn(false);

        ResponseEntity<String> response = userController.deleteUser(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).deleteUser(id);
    }

    @Test
    public void testDeleteUserDataAccessException() {
        var id = 1L;
        when(userService.existUser(id)).thenReturn(true);
        doThrow(new DataAccessException("Database Error") {}).when(userService).deleteUser(id);

        ResponseEntity<String> response = userController.deleteUser(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNameByIdSuccess() {
        var id = 1L;

        when(userService.existUser(id)).thenReturn(true);
        when(userService.getUserById(id)).thenReturn(USER_1);

        ResponseEntity<String> response = userController.getNameById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("name", response.getBody());
    }

    @Test
    public void testGetNameByIdNotFound() {
        var id = 1L;
        when(userService.existUser(id)).thenReturn(false);

        ResponseEntity<String> response = userController.getNameById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNameByIdDataAccessException() {
        var id = 1L;
        when(userService.existUser(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<String> response = userController.getNameById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testActiveAccountSuccess() {
        var id = 1L;

        when(userService.existUser(id)).thenReturn(true);
        when(userService.getUserById(id)).thenReturn(USER_1);
        when(userService.save(any(User.class))).thenReturn(USER_1);

        ResponseEntity<String> response = userController.activeAccount(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Konto zostało aktywowane", response.getBody());
        verify(userService).save(USER_1);
    }

    @Test
    public void testActiveAccountNotFound() {
        var id = 1L;
        when(userService.existUser(id)).thenReturn(false);

        ResponseEntity<String> response = userController.activeAccount(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testActiveAccountDataAccessException() {
        var id = 1L;
        when(userService.existUser(id)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<String> response = userController.activeAccount(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDisableAccountSuccess() {
        var email = DELETE_USER_DTO.email();

        when(userService.existUserByEmail(email)).thenReturn(true);
        when(userService.findByEmail(email)).thenReturn(USER_1);
        when(userService.updateUser(USER_1)).thenReturn(USER_1);

        ResponseEntity<User> response = userController.disableAccount(DELETE_USER_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEnabled());
        verify(userService).updateUser(USER_1);
    }

    @Test
    public void testDisableAccountUserNotFound() {
        var email = DELETE_USER_DTO.email();

        when(userService.existUserByEmail(email)).thenReturn(false);

        ResponseEntity<User> response = userController.disableAccount(DELETE_USER_DTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDisableAccountDataAccessException() {
        var email = DELETE_USER_DTO.email();

        when(userService.existUserByEmail(email)).thenThrow(new DataAccessException("Database Error") {});

        ResponseEntity<User> response = userController.disableAccount(DELETE_USER_DTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}