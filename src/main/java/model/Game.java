package model;

public class Game {
    String name;
    Developer developer;

    public Game(){

    }

    public Game(String name, Developer developer){
        this.name = name;
        this.developer = developer;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Developer getDeveloper(){
        return developer;
    }

    public void setDeveloper(Developer developer){
        this.developer = developer;
    }

    public boolean equals(Object o){
        return (o instanceof Game && ((Game)o).name.equals(name));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name=" + getName() + "\n");
        sb.append("Developer=" + getDeveloper());
        return sb.toString();
    }
}
