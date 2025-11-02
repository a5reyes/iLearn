package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gradebook extends Assignments {
    //List of map for each assignment, with key=assignment, val=map of student, grade
    //ex. [{"Project1": {"Alyssa": 76}}, {"Project2": {"Jaden": 42}}]
    private List<Map<String, Map<String, Float>>> gradeBookList;
 
	public Gradebook(List<Map<String, Map<String, Float>>> gradeBookList, List<String> assignments, int classroomId, String teacher, String discussions, String meetingTime) {
        super(assignments, classroomId, teacher, discussions, meetingTime);
        this.gradeBookList = gradeBookList;
    }

    public List<Map<String, Map<String, Float>>> getClassroomGradebook() {
		return gradeBookList;
	}
	public void setClassroomGradebook(List<Map<String, Map<String, Float>>> gradeBook) {
		this.gradeBookList = gradeBook;
	}

    public List<Map<String, Float>> getAssignmentGradebook(User user, String assignment) {
        List<Map<String, Float>> assignmentGradeList = new ArrayList<>();
        if(user.getIsTeacher()){
            if(checkAssignment(assignment)){
                for(Map<String, Map<String, Float>> assignmentMap : gradeBookList){
                    assignmentGradeList.add(assignmentMap.get(assignment));
                }
            }
        }
		return assignmentGradeList;
	}
    
    public void addAssignmentGrade(User user, String assignment, String student, Float grade){
        if(user.getIsTeacher()){
            Map<String, Float> studentGradeMap = new HashMap<>();
            if(checkAssignment(assignment)){
                studentGradeMap.put(student, grade);
                //have to create a new map for every assignment grade 
                //because otherwise a new assignment grade would replace an old one if new one inserted
                Map<String, Map<String, Float>> assignmentStudentGrade = new HashMap<>();
                assignmentStudentGrade.put(assignment, studentGradeMap);
                if(!this.gradeBookList.contains(assignmentStudentGrade)){
                    gradeBookList.add(assignmentStudentGrade);
                }
            }  
        }
    }

    public void editAssignmentGrade(User user, String assignment, String student, Float grade, Boolean toRemove){
        if(user.getIsTeacher()){
            Map<String, Float> updatedGradeMap = new HashMap<>();
            if(checkAssignment(assignment)){
                updatedGradeMap.put(student, grade);
                Iterator<Map<String, Map<String, Float>>> iterator = gradeBookList.iterator();
                while(iterator.hasNext()){
                    Map<String, Map<String, Float>> assignmentMap = iterator.next();
                    if(toRemove){
                        iterator.remove();
                    } else {
                        //get the assignment key; check if the assignment found equals the assignment given
                        Set<String> assignmentSet = assignmentMap.keySet();
                        String assignmentKey = !assignmentSet.isEmpty() ? assignmentSet.iterator().next() : null;
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

}

