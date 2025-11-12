package com.ilearn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gradebook {                                   
    //List of map for each assignment, with key=assignment, val=map of student, grade
    //ex. [{"Project1": {"Alyssa": 76}}, {"Project2": {"Jaden": 42}}]
    private List<Map<Assignment, Map<String, Float>>> gradeBookList = new ArrayList<>();
    private Classroom classroom;
    Connection connection = Main.connect();
 
	Gradebook(Classroom classroom) {
        this.classroom = classroom;
    }

    public List<Map<String, Float>> getAssignmentGradebook(User user, Assignment assignment) {
        List<Map<String, Float>> assignmentGradeList = new ArrayList<>();
        if(user.getIsTeacher()){
            if(classroom.checkAssignment(assignment)){
                for(Map<Assignment, Map<String, Float>> assignmentMap : gradeBookList){
                    assignmentGradeList.add(assignmentMap.get(assignment));
                }
            }
        }
		return assignmentGradeList;
	}
    
    public void addAssignmentGrade(User user, Assignment assignment, String student, Float grade){
        if(user.getIsTeacher()){
            Map<String, Float> studentGradeMap = new HashMap<>();
            if(classroom.checkAssignment(assignment)){
                studentGradeMap.put(student, grade);
                //have to create a new map for every assignment grade 
                //because otherwise a new assignment grade would replace an old one if new one inserted
                Map<Assignment, Map<String, Float>> assignmentStudentGrade = new HashMap<>();
                assignmentStudentGrade.put(assignment, studentGradeMap);
                if(!this.gradeBookList.contains(assignmentStudentGrade)){
                    gradeBookList.add(assignmentStudentGrade);
                }
            }  
        }
    }

    public void editAssignmentGrade(User user, Assignment assignment, String student, Float grade, Boolean toRemove){
        if(user.getIsTeacher()){
            Map<String, Float> updatedGradeMap = new HashMap<>();
            if(classroom.checkAssignment(assignment)){
                updatedGradeMap.put(student, grade);
                Iterator<Map<Assignment, Map<String, Float>>> iterator = gradeBookList.iterator();
                while(iterator.hasNext()){
                    Map<Assignment, Map<String, Float>> assignmentMap = iterator.next();
                    if(toRemove){
                        iterator.remove();
                    } else {
                        //get the assignment key; check if the assignment found equals the assignment given
                        Set<Assignment> assignmentSet = assignmentMap.keySet();
                        Assignment assignmentKey = !assignmentSet.isEmpty() ? assignmentSet.iterator().next() : null;
                        if(assignmentKey.equals(assignment)){
                            //get student-grades map from assignment
                            Map<String, Float> assignmentGradeMap = assignmentMap.get(assignmentKey);
                            Set<String> studentKeySet = assignmentGradeMap.keySet();
                            String studentKey = !studentKeySet.isEmpty() ? studentKeySet.iterator().next() : null;
                            if(studentKey.equals(student)){
                                //if student found, update the grade
                                assignmentGradeMap.put(studentKey, grade);
                            }
                        }
                    }
                }
            }
        }  
    }

    public void connectToDatabase(User user){
		try(Statement statement = connection.createStatement()){
            String checkIfInGradebook = "SELECT * FROM gradebooks WHERE user_id = ? AND class_id = ? AND class_name = ?";
            PreparedStatement pstmtIfInGradeBook = connection.prepareStatement(checkIfInGradebook);
			pstmtIfInGradeBook.setInt(1, user.getId());
			pstmtIfInGradeBook.setInt(2, this.classroom.getClassroomId());
			pstmtIfInGradeBook.setString(3, this.classroom.getClassroomName());
            try (ResultSet rs = pstmtIfInGradeBook.executeQuery()) {
                if (!rs.next()) {
                    String insertNewGradebook = "INSERT INTO gradebooks (user_id, class_id, class_name) VALUES (?, ?, ?)";
                    PreparedStatement pstmtNewGradebook = connection.prepareStatement(insertNewGradebook);
                    pstmtNewGradebook.setInt(1, user.getId());
			        pstmtNewGradebook.setInt(2, this.classroom.getClassroomId());
                    pstmtNewGradebook.setString(3, this.classroom.getClassroomName());
                    pstmtNewGradebook.executeUpdate(); 
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	


}

