package model;

import java.util.ArrayList;
import java.util.List;

public class Developer {
    private List<Order> orders;
    private String username;
    private String password;
    private String role;
    private List<Game> games;

    public Developer(){

    }

    public Developer(Developer dev){
        this.orders = dev.orders;
        this.username = dev.username;
        this.password = dev.password;
        this.role = dev.role;
        this.games = dev.games;
    }

    public Developer(String username, String password){
        this.username = username;
        this.password = password;
        this.role = "Developer";
        this.games = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public List<Game> getGames(){
        return games;
    }

    public List<Order> getOrders(){
        return orders;
    }

    public void addGame(Game game){
        this.games.add(game);
    }

    public boolean equals(Object o){
        return (o instanceof User && ((Developer)o).username.equals(username));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Username=" + getUsername() + "\n");
        sb.append("Role=" + getRole() + "\n");
        sb.append("Games=" + String.valueOf(getGames()) + "\n");
        sb.append("Orders=" + String.valueOf(getOrders()));
        return sb.toString();
    }
}
