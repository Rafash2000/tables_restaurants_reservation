package com.app.tables_reservations.model.dto;

import com.app.tables_reservations.model.Employee;
import com.app.tables_reservations.model.Restaurant;
import com.app.tables_reservations.model.User;

public record CreateEmployeeDto(Long userId, Long restaurantId) {
    public Employee toEmployee(User user, Restaurant restaurant) {
        return new Employee(user, restaurant);
    }
}
