package org.example.rodeodrivediner_webapp.controllers;

import jakarta.validation.Valid;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.exceptions.UserAlreadyExistsException;
import org.example.rodeodrivediner_webapp.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Customer user) {
        try {
            Customer added = customerService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>("ERROR_USER_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAllCustomers();
    }

}
