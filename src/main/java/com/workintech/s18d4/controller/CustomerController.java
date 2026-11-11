package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping({"/customer", "/customers"})
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getSalary()
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {

        List<CustomerResponse> customers = customerService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(customers);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> find(@PathVariable Long id) {

        Customer customer = customerService.find(id);

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + id
            );
        }

        return ResponseEntity.ok(toResponse(customer));
    }

    // SAVE
    @PostMapping
    public ResponseEntity<CustomerResponse> save(
            @RequestBody Customer customer) {

        Customer savedCustomer = customerService.save(customer);

        return ResponseEntity.ok(toResponse(savedCustomer));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @RequestBody Customer customer) {

        Customer existingCustomer = customerService.find(id);

        if (existingCustomer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + id
            );
        }

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setSalary(customer.getSalary());

        if (customer.getAddress() != null) {
            existingCustomer.setAddress(customer.getAddress());
        }

        Customer updatedCustomer = customerService.save(existingCustomer);

        return ResponseEntity.ok(toResponse(updatedCustomer));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerResponse> delete(@PathVariable Long id) {

        Customer customer = customerService.find(id);

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + id
            );
        }

        Customer deletedCustomer = customerService.delete(id);

        if (deletedCustomer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + id
            );
        }

        return ResponseEntity.ok(toResponse(deletedCustomer));
    }
}