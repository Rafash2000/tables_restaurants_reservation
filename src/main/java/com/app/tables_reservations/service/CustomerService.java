package com.app.tables_reservations.service;

import com.app.tables_reservations.model.Customer;
import com.app.tables_reservations.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }

    public Customer getCustomerById(Long id) { return customerRepository.findById(id).orElse(null); }

    public Customer addCustomer(Customer customer) { return customerRepository.save(customer); }

    public boolean existCustomer(Long id) { return customerRepository.existsById(id); }

    public void deleteCustomer(Long id) { customerRepository.deleteById(id); }
}
