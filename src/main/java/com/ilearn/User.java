package com.ilearn;
import java.util.ArrayList;
public class User {
    private int id;
    private String password;
    private Boolean isTeacher;
    private String name;
    private String[] classroomsNames;
    ArrayList<Classroom> classroomArr = new ArrayList<>(); 

    public User(int id, String password, Boolean isTeacher, String name, String[] classroomsNames) {
        this.id = id;
        this.password = password;
        this.isTeacher = isTeacher;
        this.name = name;
        this.classroomsNames = classroomsNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public Boolean getIsTeacher() {
        return this.isTeacher;
    }

    public void setIsTeacher(Boolean val){
        this.isTeacher = val;
    }

    //user's classrooms names setter
    public void setClassroomsNames(String[] classroomsNames) {
        this.classroomsNames = classroomsNames;
    }
    
    //user's classrooms names getter
    public String getClassroomsNames() {
        return String.join(", ", this.classroomsNames);
    }

    public void addClassroom(Classroom classroom) {
        classroomArr.add(classroom);
    }

    public ArrayList<Classroom> getClassrooms() {
        return this.classroomArr;
    }

    public static void main(String[] args) {
        //
    }
}