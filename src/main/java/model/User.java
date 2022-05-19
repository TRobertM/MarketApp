package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String role;
    private List<Game> games;

    public User(){

    }

    public User(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
        this.games = new ArrayList<>();
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

    public boolean equals(Object o){
        return (o instanceof User && ((User)o).username.equals(username));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Username=" + getUsername() + "\n");
        sb.append("Role=" + getRole() + "\n");
        sb.append("Games=" + String.valueOf(getGames()));
        return sb.toString();
    }
}