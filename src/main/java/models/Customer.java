package models;

import java.util.HashMap;

public class Customer {
    public int id;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String address;
    public String postal;
    public String city;
    public String credit;
    public String expiration;
    public String cvv;

    // Product Id, expiration
    public HashMap<Integer, String> customerProducts = new HashMap<Integer, String>();
}
