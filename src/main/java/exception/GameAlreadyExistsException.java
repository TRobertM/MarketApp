package exception;

public class GameAlreadyExistsException extends Exception {

    public GameAlreadyExistsException(String name) {
        super(String.format("A game %s already exists!", name));
    }
}