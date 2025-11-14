package com.ilearn;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;
    private String userName;
    private Integer userID;

    public CalendarActivity(ZonedDateTime date, String userName, Integer userID) {
        this.date = date;
        this.userName = userName;
        this.userID = userID;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "CalenderActivity{" +
                "date=" + date +
                ", userName='" + userName + '\'' +
                ", userID=" + userID +
                '}';
    }
}