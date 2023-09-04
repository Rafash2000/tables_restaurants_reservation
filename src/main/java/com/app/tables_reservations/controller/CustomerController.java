package com.app.tables_reservations.controller;

import com.app.tables_reservations.model.Customer;
import com.app.tables_reservations.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            return ResponseEntity.ok(customerService.getAllCustomers());
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            if (!customerService.existCustomer(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(customerService.getCustomerById(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(customerService.addCustomer(customer));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            if (!customerService.existCustomer(id)) {
                return ResponseEntity.notFound().build();
            }

            customerService.deleteCustomer(id);

            return ResponseEntity.ok("Customer with ID: %d deleted successfully".formatted(id));
        } catch (DataAccessException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
