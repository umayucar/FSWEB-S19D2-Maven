package com.workintech.s18d4.service;

import com.workintech.s18d4.repository.AddressRepository;
import com.workintech.s18d4.repository.CustomerRepository;
import com.workintech.s18d4.entity.Address;
import com.workintech.s18d4.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }
    @Override
    public List<Address> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address getById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Address not found: " + id
                ));
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address update(Long id, Address address) {
        Address existing = getById(id);

        existing.setStreet(address.getStreet());
        existing.setNo(address.getNo());
        existing.setCity(address.getCity());
        existing.setCountry(address.getCountry());
        existing.setDescription(address.getDescription());

        return addressRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Address address = getById(id);

        // Address silinince Customer silinmemeli.
        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            if (customer.getAddress() != null
                    && id.equals(customer.getAddress().getId())) {

                customer.setAddress(null);
                customerRepository.save(customer);
            }
        }

        addressRepository.delete(address);
    }
}
