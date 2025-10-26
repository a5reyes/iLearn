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
        //profile
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel profile = new JLabel("Profile");
        profilePanel.add(profile);

        //calendar
        JPanel calendarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel calendar = new JLabel("Calendar");
        calendarPanel.add(calendar);
        //JTextField calTextPart = new JTextField();
        //messages
        JPanel messagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel messages = new JLabel("Messages");
        messagesPanel.add(messages);
        //JTextField messTextPart = new JTextField();
        //classrooms
        JPanel classroomsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel classrooms = new JLabel("Classrooms");
        classroomsPanel.add(classrooms);
        //JTextField classTextPart = new JTextField();

        frame.setLayout(new BorderLayout());
        frame.add(profilePanel, BorderLayout.WEST);
        frame.add(calendarPanel, BorderLayout.EAST);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(messagesPanel);
        southPanel.add(classroomsPanel);
        frame.add(southPanel, BorderLayout.SOUTH);
    
    
        frame.setVisible(true); 
    }

    public static void main(String[] args) { 
        /*
        try (MongoClient mongoClient = MongoClients.create("...")) {
            MongoDatabase database = mongoClient.getDatabase("ilearn");
            System.out.println("Connected to the database: " + database.getName());
        }
        */  
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}