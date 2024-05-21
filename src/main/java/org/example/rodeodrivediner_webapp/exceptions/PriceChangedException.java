package org.example.rodeodrivediner_webapp.exceptions;

import org.example.rodeodrivediner_webapp.entities.Product;

public class PriceChangedException extends Exception{

    public PriceChangedException(Product prod){
        super("Price of product "+ prod.getProdId()+ " has changed");
    }
}
