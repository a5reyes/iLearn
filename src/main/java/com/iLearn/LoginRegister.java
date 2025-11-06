package com.iLearn;
import java.awt.*;
import javax.swing.*;

import com.example.User;
import com.example.SetUser;
import java.sql.*;
import java.util.Random;

public class LoginRegister extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private boolean isTeacher = false;
    Connection connection = Main.connect();

    //constructor; sets up main panel where you can login or move to register
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

    //login panel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

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

        //listening to responses on login page; otherwise switching to register if register button clicked
        switchToRegister.addActionListener(e -> cardLayout.show(mainPanel, "register"));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!username.equals("") && !password.equals("")) {
                if(isRegistered(username, password)){
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    SwingUtilities.getWindowAncestor(panel).dispose();
                    User loginUser = new User(0, password, null, username, null);
                    loginUser.getFromDatabase(username, password, loginUser);
                    SetUser currentUser = new SetUser();
                    currentUser.setUser(loginUser);
                    Main.HomePage(currentUser); 
                } else {
                    JOptionPane.showMessageDialog(this, "User not found. Please register");
                    cardLayout.show(mainPanel, "register");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });
        return panel;
    }

    //register panel
    private JPanel createRegisterPanel() {
        Random rand = new Random();
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField classesField = new JTextField();
        JCheckBox teacherCheckBox = new JCheckBox("Teacher");
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
        panel.add(teacherCheckBox);
        teacherCheckBox.addItemListener(e -> {
            if (teacherCheckBox.isSelected()) {
                isTeacher = true;
            } else {
                isTeacher = false;
            }
        });
        //listening to responses on register page; otherwise switching to login if login button clicked
        switchToLogin.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String classes = classesField.getText();
            int id = Math.abs(rand.nextInt() + 1);
            if (!username.equals("") && !password.equals("") && !classes.equals("")) {
                if(isRegistered(username, password)){ 
                    JOptionPane.showMessageDialog(this, "Already registered! Login!");
                    SwingUtilities.getWindowAncestor(panel).dispose();
                    cardLayout.show(mainPanel, "login");
                } else {
                    User user = new User(id, password, isTeacher, username, classes.split("[,;|\\s]+"));
                    user.connectToDatabase();
                    for(String enteredClass : classes.split("[,;|\\s]+")){
                        Integer classId = 0;
                        try {
                            classId = Integer.parseInt(enteredClass.replaceAll("\\D", ""));  
                        } catch (NumberFormatException error){
                            classId = Math.abs(rand.nextInt() + 1);
                        }
                        String[] newDiscussion = {"Hello! Check syllabus"};
                        Classroom course = new Classroom(enteredClass, classId, "Dr. Poonam Kumari", newDiscussion, "TR 12:30PM ~ 2:00PM & T 3:15PM ~ 4:00PM");
                        course.connectToDatabase(user);
                    }
                    JOptionPane.showMessageDialog(this, "Registered user: " + username);
                    cardLayout.show(mainPanel, "login");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });
        return panel;
    }

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void login(String username, String password) {
        if (!isRegistered(username, password)) {
            showMessage("Please register");
        }
    }

    private boolean isRegistered(String username, String pw) {
        try(Statement statement = connection.createStatement()){
            String findUser = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement pstmtFindUser = connection.prepareStatement(findUser);
            pstmtFindUser.setString(1, username);
            pstmtFindUser.setString(2, pw);
            try (ResultSet res = pstmtFindUser.executeQuery()){
                while (res.next()){
                    String name = res.getString("name");
                    String password = res.getString("password");
                    return (username.equals(name) && password.equals(pw));
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
        return false;
    }
}
