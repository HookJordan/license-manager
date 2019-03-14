package repository;

import com.google.gson.Gson;
import models.Customer;
import models.Product;

import java.util.HashMap;
import java.util.logging.Logger;

public class ProductRepository {
    final static Logger log = Logger.getAnonymousLogger();

    private static ProductRepository productRepository;
    private static Gson gson = new Gson();

    private final String PATH_REPOSITORY_LOCATION = "products.db";
    public HashMap<Integer, Product> productList = new HashMap<Integer, Product>();

    public static ProductRepository getInstance() {
        if(productRepository == null) {
            productRepository = new ProductRepository();
        }

        return productRepository;
    }

    private ProductRepository() {
        this.loadRecords();
    }

    private void loadRecords() {
        // TODO: write code to load user db
    }

    public Product addRecord(Product product) {
        // Generate id...

        // Add id to product

        // add product to list
        this.productList.put(product.id, product);

        // Save changes to db
        this.saveRecords();

        // return the product
        return product;
    }

    private void saveRecords() {
        // TODO: write code to export all records
    }

    // TODO: Write searching code for records
}
