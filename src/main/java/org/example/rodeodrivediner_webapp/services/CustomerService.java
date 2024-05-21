package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.exceptions.UserAlreadyExistsException;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Customer registerUser(Customer cust) throws UserAlreadyExistsException {
        if ( customerRepository.existsByEmail(cust.getEmail()) ) {
            throw new UserAlreadyExistsException();
        }
        return customerRepository.save(cust);
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


}
