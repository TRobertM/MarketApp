package services;

import controllers.developerAddController;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//Reads information from config.properties to create connections to the database
public class ConnectionService {
    static Connection connection;

    public static Connection Connect(){
        try{
            InputStream input = developerAddController.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            if (input == null){
                System.out.println("Sorry");
            }
            prop.load(input);
            connection = DriverManager.getConnection(prop.getProperty("url"),prop.getProperty("user"),prop.getProperty("password"));
            return connection;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
