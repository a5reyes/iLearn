package com.iLearn;

public class SetUser {
    private User currentUser;
    public void setUser(User currUser){
        currentUser = currUser;
    }
    public User getUser(){
        return currentUser;
    }
}
