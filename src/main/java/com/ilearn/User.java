package com.ilearn;
import java.util.ArrayList;

import com.ilearn.dao.ClassroomDAO;
public class User {
    private int id;
    private String password;
    private Boolean isTeacher;
    private String name;
    ArrayList<Classroom> classroomArr = new ArrayList<>(); 

    public User(int id, String password, Boolean isTeacher, String name) {
        this.id = id;
        this.password = password;
        this.isTeacher = isTeacher;
        this.name = name;
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

    public void addClassroom(Classroom classroom) {
        classroomArr.add(classroom);
    }

    public ArrayList<Classroom> getClassrooms(ClassroomDAO classroomDAO) {
        if(this.classroomArr == null){
            classroomDAO.getClassrooms(this);
        }
        return this.classroomArr;
    }

    public void setClassrooms(ArrayList<Classroom> classrooms) {
        this.classroomArr = classrooms;
    }

    public static void main(String[] args) {
        //
    }
}