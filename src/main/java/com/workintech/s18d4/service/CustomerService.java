package com.workintech.s18d4.service;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    Customer find(Long id);

    Customer save(Customer customer);

    Customer delete(Long id);

    List<CustomerResponse> getAll();

    CustomerResponse getById(Long id);

    CustomerResponse update(Long id, Customer customer);
}