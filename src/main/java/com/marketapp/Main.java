package main.java.com.marketapp;
import java.util.ArrayList;
import java.util.Objects;

class Game {
    private String name;
    private int price;
    private String lstat; ///Games can only be listed as free or paid games
    public Game(String name) {
        this.name=name;
    }
    public void setPrice() {
        this.price=0;
    }
    public void setPrice(int price) {
        this.price=price;
    }
    public void setLstat() {
        if (this.price == 0)
            this.lstat = "free";
        else
            this.lstat = "paid";
    }
    public boolean equals(Object a) {
        if(this==a)
            return true;
        if(a==null)
            return false;
        if(getClass()!=a.getClass())
            return false;
        Game g=(Game) a;
        return Objects.equals(name,price) && Objects.equals(this.name,this.price);
    }
    public String getName() {
        return this.name;
    }
    public int getPrice() {
        return this.price;
    }
    public String getLstat() {
        return this.lstat;
    }
    public String toString() {
        return "Game:"+this.getName()+" "+"Price:"+this.getPrice()+" "+"Game status:"+this.getLstat()+"\n";
    }
}

class Library {
    ArrayList<Game>gameList;
    public  Library() {
        this.gameList=new ArrayList<>();
    }
    public void addGame(Game g) {
        boolean found=false;
        for(Game a:this.gameList)
            if (a.equals(g)) {
                found=true;
               break;
            }
        if(found==true)
            System.out.println("This game already exists");
        else {
            this.gameList.add(g);
            System.out.println("Game added succesfully");
        }
    }
    public void removeGame(Game g) {
        boolean found = false;
        for (Game a : this.gameList)
            if (a.equals(g)) {
                found = true;
                break;
            }
        if(found==true) {
            this.gameList.remove(g);
            System.out.println("Game removed succesfully");
        }
        else
            System.out.println("This game does not exist.");
    }
    public void gameStore() {
        for(Game a:this.gameList)
            System.out.println(a);
    }
}


public class Main {
    public static void main(String[] args){
        Game a=new Game("Elder Scrolls");
        a.setPrice();
        a.setLstat();
        System.out.println(a);
        Game b=new Game("Chicken Invaders");
        b.setPrice(50);
        b.setLstat();
        Game c=new Game("Metal Gear Rising:Revengeance");
        c.setPrice(100);
        c.setLstat();
        Library l=new Library();
        l.addGame(a);
        l.addGame(b);
        l.removeGame(a);
        l.addGame(c);
       /// l.addGame(a);
        l.gameStore();
        ///System.out.println("Hello world");
    }
}
