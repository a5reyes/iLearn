package com.example;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JUnitTests {
    //IMPORTANT --------- use mvn test --------
    //test logging in without registering first
    @Test
    public void testLoginWithNoRegister() {
        class TestLogin extends LoginRegister {
            String message;
            @Override
            protected void showMessage(String message) {
                this.message = message;
            }
        }
        TestLogin login = new TestLogin();
        login.login("user", "abc");

        assertEquals("Please register", login.message);
    }
    /*
    //test no credentials when registering
    @Test
    public void testNoCredRegister() {
        //
    }

    //test invalid credentials when registering
    @Test
    public void testRegister() {
        //
    }    
    */
}
