package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {
    public static String checkCredentials(String username, String password) throws SQLException {
        try (Connection connection = DatabaseConnectionService.getConnection()) {
            PreparedStatement checkCredentials = connection.prepareStatement("SELECT username, password, role FROM users WHERE username = ?");
            checkCredentials.setString(1,username);
            try(ResultSet userRecord = checkCredentials.executeQuery()){
                while(userRecord.next()){
                    String storedPassword = userRecord.getString("password");
                    String role = userRecord.getString("role");
                    if(DeveloperService.encodePassword(username, password).equals(storedPassword)){
                        return role;
                    }
                }
            }
        }
        return "None";
    }
}
