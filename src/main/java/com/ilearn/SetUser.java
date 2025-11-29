package com.ilearn;

public class SetUser { 
    //class to be able to pass the user object created on loginRegister pg into the main app
    private User currentUser;
    public void setUser(User currUser){
        currentUser = currUser;
    }
    public User getUser(){
        return currentUser;
    }
}
