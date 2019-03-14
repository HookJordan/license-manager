package repository;

import com.google.gson.Gson;
import models.Customer;
import org.json.JSONArray;

import javax.swing.*;
import java.util.HashMap;
import java.util.logging.Logger;

public class CustomerRepository {
    final static Logger log = Logger.getAnonymousLogger();

    private static CustomerRepository customerRepository;
    private static Gson gson = new Gson();

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
            JSONArray userArray = new JSONArray(dbAsString);
            if (userArray.length() == 0) {
                JOptionPane.showMessageDialog(null, "The user customer is empty or corrupt. Please refer to the README for assistance.", "Customer DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Customer addCustomer(Customer customer) {
        // generate id

        // add id to customer

        // add customer to list
        this.customerList.put(customer.id, customer);

        // Save db changes
        this.saveRecords();

        // return customer with id
        return customer;
    }

    private void saveRecords() {
        // TODO: write code to export all records
    }

    // TODO: Write searching code for records
}
