package services;

import exception.UsernameAlreadyExistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersManagementService {

    public static boolean checkAvailability(String username) throws UsernameAlreadyExistsException {
        try (Connection connection = DatabaseConnectionService.getConnection()){
            String query = "SELECT username FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)){
                statement.setString(1, username);
                try (ResultSet foundUser = statement.executeQuery()){
                    if(!foundUser.next()){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UsernameAlreadyExistsException(username);
    }

    public static String checkRole(String username) throws SQLException {
        try (Connection connection = DatabaseConnectionService.getConnection()){
            String query = "SELECT role FROM users WHERE username = ?";
            try(PreparedStatement statement = connection.prepareStatement(query)){
                statement.setString(1, username);
                try (ResultSet foundUser = statement.executeQuery()){
                    if(foundUser.next()){
                        return foundUser.getString("role");
                    }
                }
            }
        }
        return "None";
    }
}
