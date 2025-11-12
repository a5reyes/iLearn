package com.ilearn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Roster {
    private List<String> roster = new ArrayList<>();
    private Classroom classroom;
    Connection connection = Main.connect();

	Roster(Classroom classroom) {
        this.classroom = classroom;
	}

    public List<String> getRoster() {
		return roster;
	}

	public void setRoster(User user, List<String> roster) {
        this.roster = roster;
	}

    /*
    public Boolean getStudent(String student) {
        //get student info - name
        boolean contains = this.roster.contains(student);
        return contains;
	}
    */

    public Boolean checkStudent(String student) {
        boolean contains = this.roster.contains(student);
        return contains;
	}

	public void addStudent(String student) {
        if(!checkStudent(student)){
            this.roster.add(student); 
        }
	}

    //accessible to only teacher
	public void removeStudent(User teacher, String student) {
        if(teacher.getIsTeacher()){
            if(checkStudent(student)){
                int idx = this.roster.indexOf(student);
                this.roster.remove(idx); 
            }
        }
	}

    public void connectToDatabase(User user){
		try(Statement statement = connection.createStatement()){
            String checkIfInRoster = "SELECT * FROM rosters WHERE user_id = ? AND username = ? AND class_id = ? AND class_name = ?";
            PreparedStatement pstmtIfInRoster = connection.prepareStatement(checkIfInRoster);
			pstmtIfInRoster.setInt(1, user.getId());
            pstmtIfInRoster.setString(2, user.getName());
			pstmtIfInRoster.setInt(3, this.classroom.getClassroomId());
			pstmtIfInRoster.setString(4, this.classroom.getClassroomName());
            try (ResultSet rs = pstmtIfInRoster.executeQuery()) {
                if (!rs.next()) {
                    String insertNewUser = "INSERT INTO rosters (user_id, username, class_id, class_name) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtNewUser = connection.prepareStatement(insertNewUser);
                    pstmtNewUser.setInt(1, user.getId());
                    pstmtNewUser.setString(2, user.getName());
			        pstmtNewUser.setInt(3, this.classroom.getClassroomId());
                    pstmtNewUser.setString(4, this.classroom.getClassroomName());
                    pstmtNewUser.executeUpdate(); 
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	

}
