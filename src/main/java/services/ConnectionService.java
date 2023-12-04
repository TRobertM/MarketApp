package services;

import controllers.developerAddController;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//Reads information from config.properties to create connections to the database
public class ConnectionService {
    private static Connection connection;

    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    private static void connect() throws SQLException, IOException {
        InputStream input = developerAddController.class.getClassLoader().getResourceAsStream("config.properties");
        if(input == null){
            throw new IllegalArgumentException("No config.properties found");
        }

        Properties prop = new Properties();
        prop.load(input);

        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        connection = DriverManager.getConnection(url, user, password);
    }
}
