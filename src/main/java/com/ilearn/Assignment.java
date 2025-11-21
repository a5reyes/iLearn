package com.ilearn;

import java.util.Objects;

public class Assignment {
    String assignmentName;
    String description;
    double grade;
    String name;
    String dueDateString;
    String work;

    public Assignment(String assignmentName, String description, double grade, String name, String dueDateString, String work) {
        this.assignmentName = assignmentName;
        this.description = description;
        this.grade = grade;
        this.name = name;
        this.dueDateString = dueDateString;
        this.work = work;
    }

    public String getAssignmentName() {
		return assignmentName;
	}

    public String getDueDateString() {
		return dueDateString;
	}

    @Override
    public String toString() {
        String formatStr = String.format("Assignment - %s - Description - %s - Grade - %,.2f - Name - %s - Due - %s - Work - %s", 
            assignmentName, description, grade, name, dueDateString, work);
        return formatStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Assignment)) return false;
        Assignment other = (Assignment) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
