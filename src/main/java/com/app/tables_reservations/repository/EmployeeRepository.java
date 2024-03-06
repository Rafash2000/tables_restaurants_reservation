package com.app.tables_reservations.repository;

import com.app.tables_reservations.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> getEmployeesByUserId(Long id);
    boolean existsEmployeeByUserId(Long userId);
}
