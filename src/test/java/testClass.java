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
            assertTrue(DeveloperService.addUser("Andy","alumnix"));
            ///assertTrue(DeveloperService.addUser("Andy","cicici"));
            ///UsernameAlreadyExistsException succesfullly thrown from Login!
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }
        try {
            DeveloperService.loadUsersFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(DeveloperService.login("Andy","alumnix"));
       /// if(DeveloperService.login("Andy","alumnix"))
          ///  System.out.println("Login succesful");
        ///else
           /// System.out.println("Login failed");
    }
    @Test
    void testDeveloperRegister() {
        try {
            assertTrue(DeveloperService.addUser("Robi","DND"));
           /// assertTrue(DeveloperService.addUser("Robi","DND"));
           ///UsernameAlreadyExistException succesfully thrown from DeveloperRegister!
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
            assertTrue(UserService.addUser("Arrokuda","Cuda","customer"));
           //// assertTrue(UserService.addUser("Arrokuda","Cuda","Customer"));
            ///UsernameAlreadyExistsException  succesfully thrown for CustomerRegister!
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
