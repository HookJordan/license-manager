package forms;

import models.Product;
import repository.ProductRepository;

import javax.swing.*;
import java.util.ArrayList;

public class frmCustomerProduct {

    private JPanel mainPanel;
    private JComboBox cbProduct;
    private JTextField txtExpiration;
    private JButton saveButton;
    private JButton cancelButton;

    public Integer productId;
    public String productExpiration;

    public frmCustomerProduct(JFrame parent, Integer productId, String productExpiration) {
        this.productId = productId;
        this.productExpiration = productExpiration;


        ArrayList<String> productOptions = new ArrayList<String>();
        int selectIdx = 0;
        int cnt = 0;
        for(Product p : ProductRepository.getInstance().productList.values()) {
            productOptions.add(p.name);
            if(this.productId != null && p.id == this.productId) {
                selectIdx = cnt;
            }
            cnt++;
        }

        cbProduct.setModel(new DefaultComboBoxModel(productOptions.toArray()));

        cbProduct.setSelectedIndex(selectIdx);
        txtExpiration.setText(this.productExpiration);

        cancelButton.addActionListener(e -> {
            this.productId = null;
            this.productExpiration = null;

            parent.dispose();
        });

        saveButton.addActionListener(e-> {

            // Mapping
            this.productExpiration = txtExpiration.getText().length() == 0 ? null : txtExpiration.getText();

            int selectedProduct = cbProduct.getSelectedIndex();
            this.productId = null;
            for(Product p : ProductRepository.getInstance().productList.values()) {
                if(p.name.equals(productOptions.get(selectedProduct))) {
                    this.productId = p.id;
                }
            }

            boolean isValid = true;

            if(this.productId == null || this.productExpiration == null) {
                isValid = false;
            }

            if(!isValid) {
                JOptionPane.showMessageDialog(null, "Error, Please select a product and enter an expiration date", "Invalid Date", JOptionPane.WARNING_MESSAGE);

            } else {
                parent.dispose();
            }
        });
    }

    public JPanel getPanel() {
        return this.mainPanel;
    }
}
