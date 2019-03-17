package forms;

import models.Product;
import repository.ProductRepository;

import javax.swing.*;
import java.util.ArrayList;

public class frmProductAdd {

    private JPanel mainPanel;
    private JTextField txtName;
    private JComboBox cboStatus;
    private JTextArea txtDescription;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;

    public Product model;

    public frmProductAdd(JFrame parent) {
        this.init(parent);

        model = new Product();

        // Disable delete for new products
        deleteButton.setVisible(false);
    }

    public frmProductAdd(JFrame parent, Product model) {
        this.init(parent);

        // Copy fields over
        this.model = model;

        txtName.setText(model.name);
        cboStatus.setSelectedIndex(model.status - 1);
        txtDescription.setText(model.description);
    }

    private void init(JFrame parent) {
        ArrayList<String> statusOptions = Util.getProductStatusList();
        statusOptions.remove(0); // remove any
        cboStatus.setModel(new DefaultComboBoxModel(statusOptions.toArray()));

        cancelButton.addActionListener(e -> {
            this.model = null;
            parent.dispose();
        });

        saveButton.addActionListener(e -> {
            boolean isValid = true;

            if(txtName.getText().length() == 0 || txtDescription.getText().length() == 0) {
                isValid = false;
            }

            if(!isValid) {
                JOptionPane.showMessageDialog(null, "Error, some of the data field are incorrect.", "Invalid Fields", JOptionPane.WARNING_MESSAGE);
            } else {
                this.model.name = txtName.getText();
                this.model.status = cboStatus.getSelectedIndex() + 1;
                this.model.description = txtDescription.getText();

                parent.dispose();
            }
        });

        deleteButton.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ProductRepository.getInstance().RemoveRecord(model.id);
                this.model = null;
                parent.dispose();
            }
        });


    }

    public JPanel getPanel() {
        return this.mainPanel;
    }
}
