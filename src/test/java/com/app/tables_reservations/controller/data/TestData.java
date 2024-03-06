package com.app.tables_reservations.controller.data;

import com.app.tables_reservations.controller.dto.AuthenticationDto;
import com.app.tables_reservations.model.*;
import com.app.tables_reservations.model.Table;
import com.app.tables_reservations.model.dto.*;
import com.app.tables_reservations.model.enums.Availability;
import com.app.tables_reservations.model.enums.Role;
import com.app.tables_reservations.model.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TestData {
    CreateUserDto CREATE_USER_DTO_1 = new CreateUserDto("name", "surname", "email1@email.com", "password");
    CreateUserDto CREATE_ADMIN_DTO_1 = new CreateUserDto("name", "surname", "email11@email.com", "password");

    User USER_1 = new User(1L, "name", "surname", "email1@email.com", "password",Role.user, true);
    User USER_2 = new User(2L, "name", "surname", "email2@email.com", "password",Role.user, true);
    User USER_3 = new User(3L, "name", "surname", "email3@email.com", "password",Role.user, true);
    User USER_4 = new User(4L, "name", "surname", "email4@email.com", "password",Role.user, true);
    User USER_5 = new User(5L, "name", "surname", "email5@email.com", "password",Role.user, true);

    List<User> USER_LIST = List.of(USER_1, USER_2, USER_3, USER_4, USER_5);

    DeleteUserDto DELETE_USER_DTO = new DeleteUserDto("email1@email.com");

    AuthenticationDto AUTHENTICATION_DTO_1 = new AuthenticationDto("email1@email.com", "password");

    User ADMIN_1 = new User(1L, "name", "surname", "email11@email.com", "password",Role.admin, true);

    AuthenticationDto AUTHENTICATION_DTO_ADMIN = new AuthenticationDto("email11@email.com", "password");

    CreateRestaurantDto CREATE_RESTAURANT_DTO_1 = new CreateRestaurantDto(
            "name", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(19,0)
    );

    Restaurant RESTAURANT_1 = new Restaurant(1L, "name", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(19,0), true);
    Restaurant RESTAURANT_2 = new Restaurant(2L, "name", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(19,0), true);
    Restaurant RESTAURANT_3 = new Restaurant(3L, "name", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(19,0), true);
    Restaurant RESTAURANT_4 = new Restaurant(4L, "name", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(19,0), true);
    Restaurant RESTAURANT_5 = new Restaurant(5L, "name", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(19,0), true);

    UpdateRestaurantDto UPDATE_RESTAURANT_DTO_1 = new UpdateRestaurantDto(
            "1", "city", "address", "phone",
            LocalTime.of(12,0), LocalTime.of(21,0)
    );

    List<Restaurant> RESTAURANT_LIST = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, RESTAURANT_4, RESTAURANT_5);

    List<GetRestaurantDto> RESTAURANT_DTO_LIST = List.of(RESTAURANT_1.getRestaurantDto(), RESTAURANT_2.getRestaurantDto(),
            RESTAURANT_3.getRestaurantDto(), RESTAURANT_4.getRestaurantDto(), RESTAURANT_5.getRestaurantDto());

    CreateEmployee CREATE_EMPLOYEE_1 = new CreateEmployee("email1@email.com", 1L);

    CreateEmployeeDto CREATE_EMPLOYEE_DTO_1 = new CreateEmployeeDto(1L, 1L);

    Employee EMPLOYEE_1 = new Employee(1L, USER_1, RESTAURANT_1);
    Employee EMPLOYEE_2 = new Employee(2L, USER_2, RESTAURANT_2);
    Employee EMPLOYEE_3 = new Employee(3L, USER_3, RESTAURANT_3);
    Employee EMPLOYEE_4 = new Employee(4L, USER_4, RESTAURANT_4);
    Employee EMPLOYEE_5 = new Employee(5L, USER_5, RESTAURANT_5);

    List<Employee> EMPLOYEE_LIST = List.of(EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3, EMPLOYEE_4, EMPLOYEE_5);

    Table TABLE_1 = new Table(1l, 1,4, LocalDate.now().minusDays(2),
            LocalTime.of(12,0), Availability.reserved, RESTAURANT_1);
    Table TABLE_2 = new Table(2l, 2, 4, LocalDate.now(),
            LocalTime.of(12,0), Availability.reserved, RESTAURANT_1);
    Table TABLE_3 = new Table(3l, 3, 4, LocalDate.now().plusDays(2),
            LocalTime.of(12,0), Availability.reserved, RESTAURANT_1);
    Table TABLE_4 = new Table(4l, 4, 4, LocalDate.now().plusDays(2),
            LocalTime.of(12,0), Availability.reserved, RESTAURANT_1);
    Table TABLE_5 = new Table(5l, 5, 4, LocalDate.now().plusDays(2),
            LocalTime.of(12,0), Availability.available, RESTAURANT_1);

    CreateTableDto CREATE_TABLE_DTO_1 = new CreateTableDto(1, 4, LocalDate.now().plusDays(2),
            LocalTime.of(12, 0), Availability.available, 1L);

    List<Table> TABLE_LIST = List.of(TABLE_1, TABLE_2, TABLE_3, TABLE_4, TABLE_5);

    Reservation RESERVATION_1 = new Reservation(1L, LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(12, 0)),
            4, Status.confirmed, RESTAURANT_1, TABLE_1, USER_1);
    Reservation RESERVATION_2 = new Reservation(2L, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),
            4, Status.confirmed, RESTAURANT_1, TABLE_2, USER_1);
    Reservation RESERVATION_3 = new Reservation(3L, LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(12, 0)),
            4, Status.confirmed, RESTAURANT_1, TABLE_3, USER_1);
    Reservation RESERVATION_4 = new Reservation(4L, LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(12, 0)),
            4, Status.confirmed, RESTAURANT_1, TABLE_4, USER_1);

    List<Reservation> RESERVATION_LIST = List.of(RESERVATION_1, RESERVATION_2, RESERVATION_3, RESERVATION_4);

    CreateReservationDto CREATE_RESERVATION_DTO = new CreateReservationDto(1L, 5L);

    UpdateReservationDto UPDATE_RESERVATION_DTO = new UpdateReservationDto(LocalDate.now().plusDays(3), LocalTime.of(20, 0));
}
