import javax.swing.*;
import java.awt.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Main {
    public static boolean loggedIn = false;
    public static void HomePage(){
        JFrame frame = new JFrame("iLearn");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");
        JMenuItem profileItem = new JMenuItem("Profile");
        JMenuItem classItem = new JMenuItem("Classrooms");
        JMenuItem calendarItem = new JMenuItem("Calendar");
        JMenuItem messageItem = new JMenuItem("Message");
        fileMenu.add(profileItem);
        fileMenu.addSeparator();
        fileMenu.add(classItem);
        fileMenu.addSeparator();
        fileMenu.add(calendarItem);
        fileMenu.addSeparator();
        fileMenu.add(messageItem);
        menuBar.add(fileMenu);
        JPanel panel = new JPanel();
        JPanel textPanel = new JPanel();
        JLabel label = new JLabel("iLearn");
        textPanel.add(label);
        frame.setLayout(new BorderLayout());
        frame.setJMenuBar(menuBar);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(textPanel, BorderLayout.SOUTH);
        frame.setVisible(true); 
    }

    public static void main(String[] args) {   
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://vinyldealsbot:QmXOPNFAaumrY3P1@cluster0.izmp5gi.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
            MongoDatabase database = mongoClient.getDatabase("ilearn");
            System.out.println("Connected to the database: " + database.getName());
        }
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}