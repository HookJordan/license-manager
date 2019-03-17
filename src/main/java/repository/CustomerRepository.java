package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Customer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class CustomerRepository {
    final static Logger log = Logger.getAnonymousLogger();

    private static CustomerRepository customerRepository;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final String PATH_REPOSITORY_LOCATION = "customers.db";
    public HashMap<Integer, Customer> customerList = new HashMap<Integer, Customer>();

    public static CustomerRepository getInstance() {
        if(customerRepository == null) {
            customerRepository = new CustomerRepository();
        }

        return customerRepository;
    }

    private CustomerRepository() {
        this.loadRecords();
    }

    private void loadRecords() {
        // TODO: write code to load user db
        String dbAsString = Util.fileAsString(this.PATH_REPOSITORY_LOCATION);

        if(dbAsString == null) {
            JOptionPane.showMessageDialog(null, "The customer db file is missing. Please refer to the README for assistance.", "Missing File", JOptionPane.ERROR_MESSAGE);
        } else {
            // File was found
            JSONArray customerArray = new JSONArray(dbAsString);
            if (customerArray.length() == 0) {
                JOptionPane.showMessageDialog(null, "The user customer is empty or corrupt. Please refer to the README for assistance.", "Customer DB Error", JOptionPane.ERROR_MESSAGE);
            } else {
                for(int i = 0; i < customerArray.length(); i++) {
                    JSONObject customer = customerArray.getJSONObject(i);
                    Customer c = gson.fromJson(customer.toString(), Customer.class);

                    this.customerList.put(c.id, c);
                }
            }
        }
    }

    public Customer addCustomer(Customer customer) {
        // generate id
        customer.id = this.nextId();

        // add customer to list
        this.customerList.put(customer.id, customer);

        // Save db changes
        this.saveRecords();

        // return customer with id
        return customer;
    }

    public int nextId() {
        int currentMax = 1;
        for(int key : customerList.keySet()) {
            if(key > currentMax) {
                currentMax = key;
            }
        }
        return currentMax + 1;
    }

    public void updateRecord(int id, Customer customer) {
        this.customerList.put(id, customer);
        this.saveRecords();
    }

    public void deleteRecord(int id) {
        this.customerList.remove(id);
        this.saveRecords();
    }

    private void saveRecords() {
        String dbString = gson.toJson(customerList.values());
        Util.writeFile(PATH_REPOSITORY_LOCATION, dbString);
    }

    public ArrayList<Customer> searchCustomers(String name, String email) {
        log.info("Searching Customer repository for name : " + name + ", email : " + email);

        ArrayList<Customer> results = new ArrayList<>();

        for(Customer c : this.customerList.values()) {
            String customerName = c.firstName + " " + c.lastName;
            if(name != null && email != null ){
                if(customerName.toLowerCase().contains(name.toLowerCase()) && c.email.toLowerCase().contains(email.toLowerCase())) {
                    results.add(c);
                }
            } else if (name != null) {
              if(customerName.toLowerCase().contains(name.toLowerCase())) {
                  results.add(c);
              }
            } else if (email != null) {
                if(c.email.toLowerCase().contains(email.toLowerCase())) {
                    results.add(c);
                }
            } else {
                results.add(c);
            }
        }

        return results;
    }
}
