package forms;

import models.Customer;
import models.Product;
import repository.CustomerRepository;
import repository.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class frmCustomerAdd {
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtAddress;
    private JTextField txtCity;
    private JTextField txtPostal;
    private JTextField txtCredit;
    private JTextField txtExpiration;
    private JTextField txtCVV;
    private JTable productList;
    private JButton cancelButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JPanel mainPanel;
    private JButton btnAddProduct;

    public Customer model;

    public frmCustomerAdd(JFrame parent) {
        this.model = new Customer();
        this.init(parent);
        this.deleteButton.setVisible(false);
    }

    public frmCustomerAdd(JFrame parent, Customer model) {
        this.model = model;

        this.init(parent);

        // Map fields
        txtFirstName.setText(model.firstName);
        txtLastName.setText(model.lastName);
        txtEmail.setText(model.email);
        txtPhone.setText(model.phone);
        txtAddress.setText(model.address);
        txtCity.setText(model.city);
        txtPostal.setText(model.postal);
        txtCredit.setText(model.credit);
        txtExpiration.setText(model.expiration);
        txtCVV.setText(model.cvv);
    }

    private void init(JFrame parent) {
        productList.setDefaultEditor(Object.class, null);
        loadLicenseTable();

        saveButton.addActionListener(e -> {
            boolean isValid = true;

            if(txtFirstName.getText().length() == 0 || txtLastName.getText().length() == 0 || txtEmail.getText().length() == 0 || txtPhone.getText().length() == 0) {
                isValid = false;
            }

            if(!isValid) {
                JOptionPane.showMessageDialog(null, "Error, some of the data field are incorrect.", "Invalid Fields", JOptionPane.WARNING_MESSAGE);
            } else {
                this.model.firstName = txtFirstName.getText();
                this.model.lastName = txtLastName.getText();
                this.model.email = txtEmail.getText();
                this.model.phone = txtPhone.getText();
                this.model.address = txtAddress.getText();
                this.model.city = txtCity.getText();
                this.model.postal = txtPostal.getText();
                this.model.credit = txtCredit.getText();
                this.model.expiration = txtExpiration.getText();
                this.model.cvv = txtCVV.getText();

                // TODO: UPDATE LICENSE LIST?

                parent.dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            this.model = null;
            parent.dispose();
        });

        deleteButton.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer profile?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                CustomerRepository.getInstance().deleteRecord(model.id);
                this.model = null;
                parent.dispose();
            }
        });

        btnAddProduct.addActionListener(e -> {
            handleLicenseForm();
        });

        productList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getClickCount() == 2) {
                    int key = productList.getSelectedRow();
                    String productName  = (String)productList.getValueAt(key, 0);
                    String expiration = (String)productList.getValueAt(key, 1);

                    Integer pid = null;
                    for(Product p : ProductRepository.getInstance().productList.values()) {
                        if(p.name.equals(productName)) {
                         pid = p.id;
                         break;
                        }
                    }

                    if(pid != null) {
                        handleLicenseForm(pid, expiration);
                    }
                }
            }
        });
    }

    private void loadLicenseTable() {
        DefaultTableModel tableModel = (DefaultTableModel)productList.getModel();

        if(tableModel.getColumnCount() == 0) {
            tableModel.addColumn("Product");
            tableModel.addColumn("Expiration");
        }

        tableModel.setRowCount(0);

        if(this.model.customerProducts.values().size() > 0) {
            for(int productId : this.model.customerProducts.keySet()) {
                String productName = ProductRepository.getInstance().productList.get(productId).name;
                tableModel.addRow(new Object[] { productName, this.model.customerProducts.get(productId) });
            }
        }
    }

    public JPanel getPanel() {
        return this.mainPanel;
    }

    private void handleLicenseForm() {
        this.handleLicenseForm(null, null);
    }

    private void handleLicenseForm(Integer productId, String expiration) {
        btnAddProduct.setEnabled(false);
        productList.setEnabled(false);

        JFrame lFrame = new JFrame("License Manager - License");
        lFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmCustomerProduct form  = new frmCustomerProduct(lFrame, productId, expiration);
        lFrame.setContentPane(form.getPanel());
        lFrame.pack();
        lFrame.setVisible(true);

        lFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);

                btnAddProduct.setEnabled(true);
                productList.setEnabled(true);

                if(form.productId != null) {
                    if(productId != null) {
                        // Update
                        model.customerProducts.put(form.productId, form.productExpiration);
                    } else {
                        if(model.customerProducts.containsKey(form.productId)) {
                            JOptionPane.showMessageDialog(null, "Error, Customer already has this product!", "Invalid Product", JOptionPane.WARNING_MESSAGE);
                        } else {
                            model.customerProducts.put(form.productId, form.productExpiration);
                        }
                    }
                    loadLicenseTable();
                }
            }
        });
    }
}
