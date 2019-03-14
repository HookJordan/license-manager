package models;

import java.util.HashMap;

public class Customer {
    public int id;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String address;
    public String postalCode;
    public String city;
    public String creditCard;
    public String expiration;
    public String cvv;

    // Product Id, expiration
    public HashMap<Integer, String> customerProducts = new HashMap<Integer, String>();
}
