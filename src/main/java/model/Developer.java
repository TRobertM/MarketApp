package model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Developer {
    private List<String> orders;
    private String username;
    private String password;
    private String role;
    private List<Game> games;
    private String profile;

    public Developer(){

    }

    public Developer(Developer dev){
        this.orders = dev.orders;
        this.username = dev.username;
        this.password = dev.password;
        this.role = dev.role;
        this.games = dev.games;
        this.profile = dev.profile;
    }

    public Developer(String username, String password, String profile){
        this.username = username;
        this.password = password;
        this.role = "Developer";
        this.games = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.profile = profile;
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

    public List<String> getOrders(){
        return orders;
    }

    public void addGame(Game game){
        this.games.add(game);
    }

    public String getProfile(){
        return profile;
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
        sb.append("Profile=" + getProfile() + "\n");
        sb.append("Orders=" + String.valueOf(getOrders()));
        return sb.toString();
    }
}
