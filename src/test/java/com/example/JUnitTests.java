package com.example;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ilearn.Assignment;
import com.ilearn.Classroom;
import com.ilearn.LoginRegister;
import com.ilearn.Main;
import com.ilearn.Roster;
import com.ilearn.User;
import com.ilearn.dao.AssignmentDAO;
import com.ilearn.dao.ClassroomDAO;
import com.ilearn.dao.UserDAO;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.DriverManager;
import java.util.ArrayList;

public class JUnitTests {
    @BeforeAll
    public static void setupDatabase() throws Exception {
        // create in-memory connection for testing
        Main.connection = DriverManager.getConnection("jdbc:sqlite:ilearn.db");
        Main.initDatabase();  // daos created
    }
    
    // IMPORTANT --------- use mvn test --------
    // test 1 - user is not registered
    @Test
    public void userIsNotRegistered() {
        LoginRegister login = new LoginRegister();
        UserDAO userDAO = login.getUserDAO();
        Boolean registeredStatus = userDAO.isRegistered("user", "abc");
        assertEquals(false, registeredStatus);
    }

    // test 2 - user is registered
    @Test
    public void userIsRegistered() {
        LoginRegister login = new LoginRegister();
        UserDAO userDAO = login.getUserDAO();
        Boolean registeredStatus = userDAO.isRegistered("user", "123");
        assertEquals(true, registeredStatus);
    }

    // test 3 - user has assignments
    @Test
    public void userAssignments(){
        AssignmentDAO assignmentDAO = Main.assignmentDAO;
        User user = new User(2138983058, "123", false, "user");
        ArrayList<Assignment> expected = new ArrayList<>();
        assertEquals(expected.getClass(), assignmentDAO.getAssignments(user, null).getClass());
    }

    // test 4 - classroom has a roster
    @Test
    public void classroomRoster(){
        ClassroomDAO classroomDAO = Main.classroomDAO;
        String[] newDiscussion = {"Hello! Check syllabus"};
        Classroom course = new Classroom("COMP350", 350, "Dr. Poonam Kumari", newDiscussion, "TR 12:30PM ~ 2:00PM & T 3:15PM ~ 4:00PM");
        Roster actual = classroomDAO.getClassroomRoster(course);
        assertEquals(new Roster(course).getClass(), actual.getClass());
    }
}
