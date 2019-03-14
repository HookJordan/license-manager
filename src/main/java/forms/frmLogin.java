package forms;

import models.User;
import repository.UserRepository;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

public class frmLogin {
    final static Logger log = Logger.getAnonymousLogger();

    private JPanel loginPanel;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel lblTrouble;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private UserRepository userRepository = UserRepository.getInstance();

    public frmLogin(JFrame parent) {
        // TODO: REMOVE THIS
        txtUsername.setText("hookjo");
        txtPassword.setText("AAAbbb###123");


        cancelButton.addActionListener(e ->  { parent.dispose(); });
        loginButton.addActionListener(e -> {

            String password = new String(txtPassword.getPassword());
            log.info("Authenticating for " + txtUsername.getText() + " : " + password);

            User targetUser = userRepository.userByName(txtUsername.getText());

            if(targetUser == null || !targetUser.password.equals(password)) {
                JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                parent.setVisible(false);

                // Login success -- show main form....
                JFrame mainFrame = new JFrame("License Manager - Home");
                mainFrame.setContentPane(new forms.frmMain(mainFrame, targetUser).getPanel());
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.pack();
                mainFrame.setSize(640, 480);
                mainFrame.setVisible(true);

                // Handle logout
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        txtUsername.setText("");
                        txtPassword.setText("");
                        parent.setVisible(true);
                    }
                });
            }
        });

        lblTrouble.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    JOptionPane.showMessageDialog(null, "Please contact the support desk for assistance in recovering your account" , "Support Desk", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JPanel getPanel() {
        return this.loginPanel;
    }
}
