package com.example;
import javax.swing.*;

public class Main{
    public static void HomePage(User user){
        App.main(null);
        //get profile username - user.getName()
        //student schedule-week at a glance; idea taken from infobear
        /*
        String[] sectionsInfo = user.viewClassrooms().split(", ");
        for(String sectionInfo : sectionsInfo){
            Integer classId = Integer.parseInt(sectionInfo.replaceAll("\\D", "")); 
            Classroom course = new Classroom(classId, "Dr. Poonam Kumari", "Hello! Check syllabus", "TR 12:30PM ~ 2:00PM & T 3:15PM ~ 4:00PM");
            JTextArea sectionLabel = new JTextArea(sectionInfo + "\n" + course.getTeacher() + "\n" + course.getMeetingTime() +  "\n" + course.getDiscussions());
            calendarPanel.add(sectionLabel);
        }
        */
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}