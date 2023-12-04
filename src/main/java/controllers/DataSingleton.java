package controllers;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private String username;

    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
