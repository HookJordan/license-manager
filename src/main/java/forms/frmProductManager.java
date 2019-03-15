package forms;

import models.Product;
import repository.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class frmProductManager {

    private JPanel productManagerFrame;
    private JTable productTable;
    private JComboBox cboStatus;
    private JTextField productNameTextField;
    private JButton searchButton;
    private JButton newProductButton;
    private JButton resetButton;

    public JPanel getPanel() {
        return this.productManagerFrame;
    }

    public frmProductManager() {
        productTable.setDefaultEditor(Object.class, null);
        loadTable();

        cboStatus.setModel(new DefaultComboBoxModel(Util.getProductStatusList().toArray()));

        searchButton.addActionListener(e -> {
            String name = productNameTextField.getText().equals("") ? null : productNameTextField.getText();
            Integer status = cboStatus.getSelectedIndex() == 0 ? null : cboStatus.getSelectedIndex();

            loadTable(name, status);
        });

        newProductButton.addActionListener(e -> {
            handleProductForm(null);
        });

        resetButton.addActionListener(e -> {
            productNameTextField.setText("");
            cboStatus.setSelectedIndex(0);
            loadTable();
        });

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getClickCount() == 2) {
                    int row = productTable.getSelectedRow();
                    int productId = (int)productTable.getValueAt(row, 0);

                    handleProductForm(ProductRepository.getInstance().productList.get(productId));
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    private  void loadTable() {
        loadTable(null, null);
    }

    private void loadTable(String nameQuery, Integer statusQuery) {
        DefaultTableModel tableModel = (DefaultTableModel)productTable.getModel();

        // Only create columns on first load
        if(tableModel.getColumnCount() == 0) {
            // Add Columns
            tableModel.addColumn("Id");
            tableModel.addColumn("Product Name");
            tableModel.addColumn("Product Status");
        }

        // Load data
        if(tableModel.getRowCount() > 0) {
            tableModel.setRowCount(0);
        }

        ArrayList<Product> results = ProductRepository.getInstance().searchProducts(nameQuery, statusQuery);

        if(results.size() == 0) {
            JOptionPane.showMessageDialog(null, "No results found", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            for(Product p : results) {
                tableModel.addRow(new Object[] {p.id, p.name, Util.statusToString(p.status)});
            }
        }
    }

    private void handleProductForm(Product model) {
        // Show product edit empty...
        newProductButton.setEnabled(false);
        productTable.setEnabled(false);

        JFrame pFrame = new JFrame("License Manager - New Product");
        pFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frmProductAdd form = model == null ? new frmProductAdd(pFrame) : new frmProductAdd(pFrame, model);
        pFrame.setContentPane(form.getPanel());
        pFrame.pack();
        pFrame.setSize(640, 480);
        pFrame.setVisible(true);

        pFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                newProductButton.setEnabled(true);
                productTable.setEnabled(true);

                if(form.model != null) {
                    if(model == null) {
                        ProductRepository.getInstance().addRecord(form.model);
                    } else {
                        ProductRepository.getInstance().UpdateRecord(model.id, form.model);
                    }
                }

                // Rerun search or default listing
                searchButton.doClick();
            }
        });
    }

}
