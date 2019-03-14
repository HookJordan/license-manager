package forms;

import models.Customer;
import models.User;
import repository.CustomerRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.logging.Logger;

public class frmMain {
    private static final Logger log = Logger.getAnonymousLogger();

    private JLabel lblWelcome;
    private JButton customerManagerButton;
    private JButton productManagerButton;
    private JButton logoutButton;
    private JTable customerList;
    private JPanel mainPanel;

    public frmMain(JFrame parent, User user) {
        lblWelcome.setText("Welcome " + user.username + " to the License Manager.");

        if(!user.roles.contains("customer-manager")) {
            // Disable customer panel
            customerManagerButton.setEnabled(false);
            customerList.setEnabled(false);
        } else {
            // Add Rows
            DefaultTableModel model = (DefaultTableModel)customerList.getModel();
            model.addColumn("Id");
            model.addColumn("Customer Name");
            model.addColumn("Email Address");
            model.addColumn("Phone Number");

            // model.addRow(new Object[] { "0001", "Jordan Hook", "hookjordan@live.com", "+1 (647) 975 2957"});
            // model.addRow(new Object[] { "0002", "Cassandra Chanderpaul", "fakeEmail@live.com", "+1 (647) 975 2957"});
            for(Customer c : CustomerRepository.getInstance().customerList.values()) {
                model.addRow(new Object[] { Util.formatId(c.id), c.firstName + " "  + c.lastName, c.email, c.phone });
            }

            customerList.setDefaultEditor(Object.class, null);

            // Handle on row click
            customerList.getSelectionModel().addListSelectionListener(e -> {
                String customerId = customerList.getValueAt(customerList.getSelectedRow(), 0).toString();
                log.info("SELECTED CUSTOMER " + customerId);

            });
        }

        if(!user.roles.contains("product-manager")) {
            // Disable product panel
            productManagerButton.setEnabled(false);
        } else {

        }

        logoutButton.addActionListener(e -> { parent.dispose(); });

    }

    public JPanel getPanel() {
        return this.mainPanel;
    }
}
