package com.ilearn;

import java.util.Objects;

public class Assignment {
    int assignmentId;
    int teacherId;
    String assignmentName;
    String description;
    double grade;
    String name;
    String dueDateString;
    String work;

    public Assignment(int assignmentId, int teacherId, String assignmentName, String description, double grade, String name, String dueDateString, String work) {
        this.assignmentId = assignmentId;
        this.teacherId = teacherId;
        this.assignmentName = assignmentName;
        this.description = description;
        this.grade = grade;
        this.name = name;
        this.dueDateString = dueDateString;
        this.work = work;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getAssignmentName() {
		return assignmentName;
	}

    public String getDueDateString() {
		return dueDateString;
	}

    public String getWork() {
		return work;
	}

    public String getStudent() {
		return name;
	}

    public Integer getTeacherId() {
		return teacherId;
	}

    public void updateWork(String updatedWork) {
		this.work = updatedWork;
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

        return Objects.equals(this.assignmentName, other.assignmentName) &&
            Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentName, name);
    }


}
