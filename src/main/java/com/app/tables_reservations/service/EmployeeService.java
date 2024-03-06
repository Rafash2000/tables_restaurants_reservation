package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Employee;
import com.app.tables_reservations.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() { return employeeRepository.findAll(); }

    public Employee getEmployeeById(Long id) { return employeeRepository.findById(id).orElse(null); }

    public Employee addEmployee(Employee employee) { return employeeRepository.save(employee); }

    public boolean existsEmployee(Long id) { return employeeRepository.existsById(id); }

    public void deleteEmployee(Long id) { employeeRepository.deleteById(id); }

    public Employee getByUserId(Long id) { return employeeRepository.getEmployeesByUserId(id).orElse(null); }

    public boolean existByUserId(Long id) { return employeeRepository.existsEmployeeByUserId(id); }
}
