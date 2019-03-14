package repository;

import com.google.gson.Gson;
import models.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
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
        String dbAsString = Util.fileAsString(this.PATH_REPOSITORY_LOCATION);

        if(dbAsString == null) {
            JOptionPane.showMessageDialog(null, "The product db file is missing. Please refer to the README for assistance.", "Missing File", JOptionPane.ERROR_MESSAGE);
        } else {
            // File was found
            JSONArray productArray = new JSONArray(dbAsString);
            if (productArray.length() == 0) {
                JOptionPane.showMessageDialog(null, "The user product is empty or corrupt. Please refer to the README for assistance.", "Product DB Error", JOptionPane.ERROR_MESSAGE);
            } else {
                for(int i = 0; i < productArray.length(); i++) {
                    JSONObject product = productArray.getJSONObject(i);
                    Product p = gson.fromJson(product.toString(), Product.class);

                    this.productList.put(p.id, p);
                }
            }
        }
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
