package org.example.rodeodrivediner_webapp.exceptions;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(){
        super("User already exists");
    }
}
