import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LoginRegister extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currUser;

    public LoginRegister() {
        setTitle("iLearn");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegisterPanel(), "register");
    
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton switchToRegister = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(switchToRegister);

        switchToRegister.addActionListener(e -> cardLayout.show(mainPanel, "register"));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!username.equals("") && !password.equals("")) {
                if(currUser == null){
                    JOptionPane.showMessageDialog(this, "Please register");
                    cardLayout.show(mainPanel, "register");
                } else {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    SwingUtilities.getWindowAncestor(panel).dispose();
                    Main.HomePage(currUser);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });
        return panel;
    }

    private JPanel createRegisterPanel() {
        Random rand = new Random();
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField classesField = new JTextField();
        JButton registerButton = new JButton("Register");
        JButton switchToLogin = new JButton("Back to Login");

        panel.add(new JLabel("Choose Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Choose Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Add Classes:"));
        panel.add(classesField);
        panel.add(registerButton);
        panel.add(switchToLogin);

        switchToLogin.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String classes = classesField.getText();
            if (!username.equals("") && !password.equals("")) {
                User user = new User(rand.nextInt() + 1, password, null, username, classes.split(","));
                currUser = user;
                JOptionPane.showMessageDialog(this, "Registered user: " + username);
                cardLayout.show(mainPanel, "login");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
            // TODO: Save to MongoDB here
        });
        return panel;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}
