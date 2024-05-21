package org.example.rodeodrivediner_webapp.controllers;

import jakarta.validation.Valid;
import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.exceptions.DateWrongRangeException;
import org.example.rodeodrivediner_webapp.exceptions.IllegalQuantityException;
import org.example.rodeodrivediner_webapp.exceptions.UserNotFoundException;
import org.example.rodeodrivediner_webapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/purchases")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity create(@RequestBody @Valid Cart cart) { // è buona prassi ritornare l'oggetto inserito
        try {
            return new ResponseEntity(cartService.addPurchase(cart), HttpStatus.OK);
        } catch (IllegalQuantityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product quantity unavailable!", e); // realmente il messaggio dovrebbe essrere più esplicativo
        }
    }

    @GetMapping("/{user}")
    public List<Cart> getPurchases(@RequestBody @Valid Customer user) {
        try {
            return cartService.getPurchasesByUser(user);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }

    @GetMapping("/{user}/{startDate}/{endDate}")
    public ResponseEntity getPurchasesInPeriod(@Valid @PathVariable("user") Customer user, @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date start,
                                               @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date end) {
        try {
            List<Cart> result = cartService.getPurchasesByUserInPeriod(user, start, end);
            if (result.isEmpty()) {
                return new ResponseEntity<>("No results!", HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found XXX!", e);
        } catch (DateWrongRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be previous end date XXX!", e);
        }
    }

}
