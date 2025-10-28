import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import javax.swing.*;

public class JUnitTests {
    //test no credentials when registering
    @Test
    public void testNoCredRegister() {
        /*
         LoginRegister registerTest = new LoginRegister();
        Class<?> clazz = registerTest.getClass();
        Method registerPanelMethod = clazz.getDeclaredMethod("createRegisterPanel", String.class);
        registerPanelMethod.setAccessible(true);
        assertEquals(registerPanelMethod.invoke(registerTest));
         */
    }

    //test invalid credentials when registering
    @Test
    public void testRegister() {
        //
    }

    //test logging in without registering first
    @Test
    public void testLoginWithNoRegister() {
        //

    }

    public static void main(String[] args){
        //
    }
}
