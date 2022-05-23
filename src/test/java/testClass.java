import exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.DeveloperService;
import services.UserService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class testClass  {
    @Test
    void testLogin(){
        try {
            DeveloperService.addUser("Andy","alumnix");
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }
        try {
            DeveloperService.loadUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(DeveloperService.login("Andy","alumnix"));
        System.out.println("Login succesful.");
       /// if(DeveloperService.login("Andy","alumnix"))
          ///  System.out.println("Login succesful");
        ///else
           /// System.out.println("Login failed");
    }
    @Test
    void testDeveloperRegister() {
        try {
            DeveloperService.addUser("Robi","DND");
            System.out.println("Developer registered succesfully.");
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }
        try {
            DeveloperService.loadUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testCustomerRegister() {
        try {
            UserService.addUser("Arrokuda","Cuda","customer");
            System.out.println("Customer registered succesfully.");
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }
        try {
            UserService.loadUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
