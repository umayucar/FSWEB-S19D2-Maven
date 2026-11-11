package com.workintech.s18d4.service;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.repository.AccountRepository;
import com.workintech.s18d4.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    // MainTest.java bu constructor'ı kullanıyor.
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = null;
    }

    // Spring uygulamada bu constructor'ı kullanacak.
    @Autowired
    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            AccountRepository accountRepository) {

        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found: " + id
                ));
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getSalary()
        );
    }

    // GET ALL

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET BY ID

    @Override
    public Customer find(Long id) {
        return findCustomer(id);
    }

    @Override
    public CustomerResponse getById(Long id) {
        return toResponse(findCustomer(id));
    }

    // SAVE

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    // UPDATE

    @Override
    @Transactional
    public CustomerResponse update(Long id, Customer customer) {

        Customer existingCustomer = findCustomer(id);

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setSalary(customer.getSalary());

        if (customer.getAddress() != null) {
            existingCustomer.setAddress(customer.getAddress());
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return toResponse(updatedCustomer);
    }

    // DELETE

    @Override
    @Transactional
    public Customer delete(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElse(null);

        if (customer == null) {
            return null;
        }

        // Müşteriye ait hesapları sil.
        if (accountRepository != null) {
            accountRepository.deleteAll(
                    accountRepository.findByCustomerId(id)
            );
        }

        // Customer -> Address CascadeType.ALL ise
        // müşterinin adresi de silinir.
        customerRepository.delete(customer);

        return customer;
    }
}