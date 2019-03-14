package repository;

import com.google.gson.Gson;
import models.Customer;

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
    }

    private void saveRecords() {
        // TODO: write code to export all records
    }

    // TODO: Write searching code for records
}
