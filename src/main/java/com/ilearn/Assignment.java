package com.ilearn;

import java.util.Objects;

public class Assignment {
    String name;
    String description;
    int grade;

    Assignment(String name, String description, int grade) {
        this.name = name;
        this.description = description;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return name;
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
