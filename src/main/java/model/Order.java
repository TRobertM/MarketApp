package model;

/*
THIS HAS NO USE ANYMORE BUT ITS KEPT HERE AS A POINT OF REFERENCE FOR WHAT WAS DONE BEFORE AND AS A HELPER FOR FUTURE PROJECTS
 */

public class Order {
    int ID;
    String customerName;
    String gameName;
    String developerName;

    public Order(){

    }

    public Order(int ID, String gameName, String customerName, String developerName){
        this.ID = ID;
        this.customerName = customerName;
        this.gameName = gameName;
        this.developerName = developerName;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String name){
        customerName = name;
    }

    public String getGameName(){
        return gameName;
    }

    public void setGameName(String name){
        gameName = name;
    }

    public String getDeveloperName(){
        return developerName;
    }

    public void setDeveloperName(String name){
        developerName = name;
    }

    public int getID(){
        return ID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer=" + getCustomerName() + "\n");
        sb.append("Game=" + getGameName());
        return sb.toString();
    }
}
