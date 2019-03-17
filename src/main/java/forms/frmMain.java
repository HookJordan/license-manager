package forms;

import models.Customer;
import models.User;
import repository.CustomerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.*;
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
    }

    private void handleCustomerForm(Customer model) {
        customerManagerButton.setEnabled(false);
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
                customerManagerButton.setEnabled(true);
                customerList.setEnabled(true);

                if(form.model != null) {
                    if(model != null) {
                        CustomerRepository.getInstance().updateRecord(model.id, form.model);
                    } else {
                        CustomerRepository.getInstance().addCustomer(form.model);
                    }
                }

                loadTable();
            }
        });
    }
}
