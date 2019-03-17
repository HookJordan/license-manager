package forms;

import models.Customer;
import models.User;
import repository.CustomerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class frmMain {
    private static final Logger log = Logger.getAnonymousLogger();

    private JLabel lblWelcome;
    private JButton customerManagerButton;
    private JButton productManagerButton;
    private JButton logoutButton;
    private JTable customerList;
    private JPanel mainPanel;
    private JLabel lblList;

    public frmMain(JFrame parent, User user) {
        lblWelcome.setText("Welcome " + user.username + " to the License Manager.");

        if(!user.roles.contains("customer-manager")) {
            // Disable customer panel
            customerManagerButton.setEnabled(false);
            customerList.setEnabled(false);
            lblList.setText("You do not have permission to see customer information.");
        } else {
            DefaultTableModel model = (DefaultTableModel)customerList.getModel();

            model.addColumn("Id");
            model.addColumn("Customer Name");
            model.addColumn("Email Address");
            model.addColumn("Phone Number");

            loadTable();
        }

        if(!user.roles.contains("product-manager")) {
            // Disable product panel
            productManagerButton.setEnabled(false);
        } else {

        }

        logoutButton.addActionListener(e -> { parent.dispose(); });

        productManagerButton.addActionListener(e -> {
            productManagerButton.setEnabled(false);

            JFrame productFrame = new JFrame("License Manager - Products");
            productFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            productFrame.setContentPane(new frmProductManager().getPanel());
            productFrame.pack();
            productFrame.setSize(640, 480);
            productFrame.setVisible(true);

            productFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    productManagerButton.setEnabled(true);
                }
            });

            productManagerButton.enable();
        });

        customerManagerButton.addActionListener(e -> {
            customerManagerButton.setEnabled(false);
            customerList.setEnabled(false);

            JFrame customerFrame = new JFrame("License Manager - Customer");
            frmCustomerManager customerManager = new frmCustomerManager();
            customerFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            customerFrame.setContentPane(customerManager.getPanel());
            customerFrame.pack();
            customerFrame.setSize(640, 480);
            customerFrame.setVisible(true);

            customerFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);

                    // Enable buttons
                    customerManagerButton.setEnabled(true);
                    customerList.setEnabled(true);

                    // Reload recent list in case it changed
                    loadTable();
                }
            });
        });
    }

    public JPanel getPanel() {
        return this.mainPanel;
    }

    private void loadTable() {
        // Add Rows
        DefaultTableModel model = (DefaultTableModel)customerList.getModel();

        model.setRowCount(0);

        ArrayList<Customer> recentCustomers = new ArrayList<Customer>(CustomerRepository.getInstance().customerList.values());
        Collections.sort(recentCustomers, Collections.reverseOrder());

        for(Customer c : recentCustomers) {
            model.addRow(new Object[] { Util.formatId(c.id), c.firstName + " "  + c.lastName, c.email, c.phone });
        }

        customerList.setDefaultEditor(Object.class, null);

        // Handle on row click
        customerList.getSelectionModel().addListSelectionListener(e -> {
            String customerId = customerList.getValueAt(customerList.getSelectedRow(), 0).toString();
            log.info("SELECTED CUSTOMER " + customerId);

        });
    }
}
