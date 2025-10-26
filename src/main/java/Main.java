import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Main {
    public static boolean loggedIn = false;
    public static void HomePage(User user){
        JFrame frame = new JFrame("iLearn");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        //profile
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel username = new JLabel("User: " + user.getName());
        userPanel.add(username);

        //calendar
        JPanel calendarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel calendar = new JLabel("Calendar");
        calendarPanel.add(calendar);
        JButton viewCalButton = new JButton("View");
        calendarPanel.add(viewCalButton);
        /* calendar to view assignments, discussions on specific dates
        viewCalButton.addActionListener(e -> {
            JCalendar calendar = new JCalendar();
            calendarPanel.add(calendar);
        });
         */
        //calendar-classroom section where classrooms are viewed at a glance
        String[] sectionsInfo = user.viewClassrooms().split(", ");
        for(String sectionInfo : sectionsInfo){
            Integer classId = Integer.parseInt(sectionInfo.replaceAll("\\D", "")); 
            Classroom course = new Classroom(classId, "Dr. Poonam Kumari", "Hello! Check syllabus", "TR 12:30PM ~ 2:00PM & T 3:15PM ~ 4:00PM");
            JTextArea sectionLabel = new JTextArea(sectionInfo + "\n" + course.getTeacher() + "\n" + course.getMeetingTime() +  "\n" + course.getDiscussions());
            calendarPanel.add(sectionLabel);
        }
        
        //JPanel classPanel = new JPanel();

        //messages
        JPanel messagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel messages = new JLabel("Messages");
        messagesPanel.add(messages);
        //JTextField messTextPart = new JTextField();

        //classrooms
        JPanel classroomsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel classrooms = new JLabel("Classrooms");
        classroomsPanel.add(classrooms);
        JButton viewClassButton = new JButton("View");
        classroomsPanel.add(viewClassButton);
        viewClassButton.addActionListener(e -> {
            String[] sections = user.viewClassrooms().split(", ");
            for(String section : sections){
                JButton sectionButton = new JButton(section);
                sectionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //open to classroom frame
                        JOptionPane.showMessageDialog(frame, "You clicked the button!");
                    }
                });
                classroomsPanel.add(sectionButton);
            }
            classrooms.setText("Classes: " + user.viewClassrooms());
        });

        frame.setLayout(new BorderLayout());
        frame.add(userPanel, BorderLayout.WEST);
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