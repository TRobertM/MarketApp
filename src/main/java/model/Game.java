package model;

public class Game {
    String name;
    String developer;

    public Game(){

    }

    public Game(String name, String developer){
        this.name = name;
        this.developer = developer;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDeveloper(){
        return developer;
    }

    public void setDeveloper(String developer){
        this.developer = developer;
    }

    public boolean equals(Object o){
        return (o instanceof Game && ((Game)o).name.equals(name));
    }

    @Override
    public String toString(){
        return getName() + ", " + getDeveloper();
    }
}
