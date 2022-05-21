package exception;

public class GameAlreadyExistsException extends Exception {

    private String name;

    public GameAlreadyExistsException(String name) {
        super(String.format("An account with the username %s already exists!", name));
        this.name = name;
    }

    public String getUsername() {
        return name;
    }
}