package forms;

import models.Customer;
import repository.CustomerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class frmCustomerManager {
    private static final Logger log = Logger.getAnonymousLogger();

    private JPanel customerManagerPanel;
    private JTable customerList;
    private JTextField customerEmailTextField;
    private JTextField customerNameTextField;
    private JButton searchButton;
    private JButton newCustomerButton;
    private JButton resetButton;

    public frmCustomerManager() {
        customerList.setDefaultEditor(Object.class, null);
        loadTable();

        resetButton.addActionListener(e -> {
            customerEmailTextField.setText("");
            customerNameTextField.setText("");

            loadTable();
        });

        newCustomerButton.addActionListener(e -> {
            handleCustomerForm(null);
        });

        searchButton.addActionListener(e -> {
            String name = customerNameTextField.getText().length() > 0 ? customerNameTextField.getText() : null;
            String email = customerEmailTextField.getText().length() > 0 ? customerEmailTextField.getText() : null;

            loadTable(name, email);
        });

        customerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getClickCount() == 2) {
                    int row = customerList.getSelectedRow();
                    int customerId = Integer.parseInt((String)customerList.getValueAt(row, 0));

                    handleCustomerForm(CustomerRepository.getInstance().customerList.get(customerId));
                }
            }
        });
    }


    public JPanel getPanel() {
        return this.customerManagerPanel;
    }

    private void loadTable() {
        loadTable(null, null);
    }

    private void loadTable(String name, String email) {
        // Add Rows
        DefaultTableModel model = (DefaultTableModel)customerList.getModel();

        if(model.getColumnCount() == 0){
            model.addColumn("Id");
            model.addColumn("Customer Name");
            model.addColumn("Email");
            model.addColumn("Phone number");
        }

        model.setRowCount(0);

        ArrayList<Customer> recentCustomers = CustomerRepository.getInstance().searchCustomers(name, email);
        Collections.sort(recentCustomers, Collections.reverseOrder());

        if(recentCustomers.size() == 0) {
            JOptionPane.showMessageDialog(null, "No results found", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            for(Customer c : recentCustomers) {
                model.addRow(new Object[] { Util.formatId(c.id), c.firstName + " "  + c.lastName, c.email, c.phone });
            }
        }

        customerList.setDefaultEditor(Object.class, null);
    }

    private void handleCustomerForm(Customer model) {
        newCustomerButton.setEnabled(false);
        customerList.setEnabled(false);

        JFrame cFrame = new JFrame("License Manager - Customer");
        cFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmCustomerAdd form = model == null ? new frmCustomerAdd(cFrame) : new frmCustomerAdd(cFrame, model);
        cFrame.setContentPane(form.getPanel());
        cFrame.pack();
        cFrame.setVisible(true);

        cFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);

                newCustomerButton.setEnabled(true);
                customerList.setEnabled(true);

                if(form.model != null) {
                    if(model != null) {
                        CustomerRepository.getInstance().updateRecord(model.id, form.model);
                    } else {
                        CustomerRepository.getInstance().addCustomer(form.model);
                    }
                }

                searchButton.doClick();
            }
        });
    }
}
