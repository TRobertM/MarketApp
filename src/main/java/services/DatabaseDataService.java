package services;

import model.Order;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseDataService {

    private static void executeUpdate(String query, Object... variables){
        try( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement(query);
            for (int i = 0; i < variables.length; i++) {
                if(variables[i] instanceof Integer){
                    statement.setInt(i+1, (Integer)variables[i]);
                } else {
                    statement.setString(i+1, (String)variables[i]);
                }
            }
            statement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertGame(String gameName, String developerName){
        executeUpdate("INSERT INTO games VALUES (?, ?)", gameName, developerName);
    }

    public static void removeGame(String gameName){
        executeUpdate("DELETE FROM games WHERE name = ?", gameName);
    }

    public static void placeOrder(String gameName, String developerName, String userName){
        executeUpdate("INSERT INTO orders VALUES (NULL, ?, ?, ?)", gameName, userName, developerName);
    }

    public static void removeOrder(int ID) {
        executeUpdate("DELETE FROM orders WHERE id = ?", ID);
    }

    public static void buyGame(String userName, String gameName, String developerName){
        executeUpdate("INSERT INTO owned VALUES (?, ?, ?)", gameName, developerName, userName);
    }

    public static void deleteGame(String gameName, String userName){
        executeUpdate("DELETE FROM owned WHERE game_name = ? and user_name = ?", gameName, userName);
    }

    public static void addToCart(String gameName, String userName){
        executeUpdate("INSERT INTO user_cart VALUES (?, ?)", gameName, userName);
    }

    public static void removeFromCart(String gameName, String userName) {
        executeUpdate("DELETE FROM user_cart WHERE game_name = ? and user_name = ?", gameName, userName);
    }

    public static void registerUser(String userName, String password, String role){
        executeUpdate("INSERT INTO users VALUES (?, ?, ?)", userName, password, role);
    }

    public static void addToWishlist(String gameName, String developer, String userName){
        executeUpdate("INSERT INTO wishlist VALUES (?, ?, ?)", gameName, developer, userName);
    }

    public static boolean gameExists(String gameName) throws SQLException, IOException {
        try( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT name FROM games WHERE name = ?");
            statement.setString(1, gameName);
            if(statement.executeQuery().next()){
                return true;
            }
        }
        return false;
    }

    public static List<String> retrieveGames(String developerName) throws SQLException {
        List<String> allGames = new ArrayList<>();
        try( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT name FROM games WHERE developer = ?");
            statement.setString(1, developerName);
            try( ResultSet games = statement.executeQuery() ){
                while(games.next()){
                    allGames.add(games.getString(1));
                }
            }
        }
        return allGames;
    }

    public static boolean hasOrders(String developerName) throws SQLException {
        try( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT id FROM orders WHERE seller = ?");
            statement.setString(1, developerName);
            try(ResultSet orders = statement.executeQuery()){
                if(orders.next()){
                    return true;
                }
            }
        }
        return false;
    }

    public static String retrieveDeveloper(String gameName) throws SQLException {
        try ( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT developer FROM games WHERE name = ?");
            statement.setString(1, gameName);
            try(ResultSet developer = statement.executeQuery()){
                if(developer.next()){
                    return developer.getString(1);
                }
            }
        }
        return null;
    }

    public static List<Order> retrieveOrders(String developer) throws SQLException {
        List<Order> allOrders = new ArrayList<>();
        try ( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT id,game,buyer FROM orders WHERE seller = ? ");
            statement.setString(1, developer);
            ResultSet orders = statement.executeQuery();
            while(orders.next()) {
                allOrders.add(new Order(orders.getInt(1), orders.getString(2), orders.getString(3), developer));
            }
        }
        return allOrders;
    }

    public static Order retrieveOrderInformation(int ID) throws SQLException{
        try ( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT buyer, game, seller FROM orders WHERE ID = ?");
            statement.setInt(1, ID);
            ResultSet info = statement.executeQuery();
            if(info.next()){
                return new Order(ID, info.getString(2), info.getString(1), info.getString(3));
            }
            return null;
        }
    }

    public static int retrieveNumberOfOrders(String developer) throws SQLException{
        int numberOfOrders = 0;
        try ( Connection con = DatabaseConnectionService.getConnection() ){
            PreparedStatement statement = con.prepareStatement("SELECT id FROM orders WHERE seller = ?");
            statement.setString(1, developer);
            try ( ResultSet totalOrders = statement.executeQuery() ){
                while(totalOrders.next()){
                    numberOfOrders++;
                }
            }
        }
        return numberOfOrders;
    }
}
