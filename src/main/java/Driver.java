import javax.swing.*;

public class Driver {

    public static void main(String[] args) {
        // Show login form
        JFrame loginFrame = new JFrame("License Manager - Login");
        loginFrame.setContentPane(new forms.frmLogin(loginFrame).getPanel());
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);
    }
}
