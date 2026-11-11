package com.workintech.s18d4.service;

import com.workintech.s18d4.entity.Address;

import java.util.List;

public interface AddressService {

    List<Address> getAll();

    Address getById(Long id);

    Address save(Address address);

    Address update(Long id, Address address);

    void delete(Long id);
}