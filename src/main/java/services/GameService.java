/*
THIS HAS NO USE ANYMORE BUT ITS KEPT HERE AS A POINT OF REFERENCE FOR WHAT WAS DONE BEFORE AND AS A HELPER FOR FUTURE PROJECTS
 */

//package services;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import exception.CouldNotWriteUsersException;
//import exception.GameAlreadyExistsException;
//import model.Developer;
//import model.Game;
//import org.apache.commons.io.FileUtils;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Objects;
//import java.util.ArrayList;


//public class GameService {

//    public static List<Game> games = new ArrayList<>();
//    private static final Path GAMES_PATH = FileSystemService.getPathToFile("config", "games.json");
//
//    public static void addGame(String name, String developer) throws GameAlreadyExistsException {
//        checkGameDoesNotAlreadyExist(name);
//        games.add(new Game(name, developer));
//        persistUsers();
//    }
//
//    public static boolean loadgamesfromfile() throws IOException {
//        if (!Files.exists(GAMES_PATH)) {
//            FileUtils.copyURLToFile(GameService.class.getClassLoader().getResource("gamez.json"), GAMES_PATH.toFile());
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        games = objectMapper.readValue(GAMES_PATH.toFile(), new TypeReference<>() {
//        });
//        return true;
//    }
//
//    private static void checkGameDoesNotAlreadyExist(String name) throws GameAlreadyExistsException {
//        for (Game game : games) {
//            if (Objects.equals(name, game.getName()))
//                throw new GameAlreadyExistsException(name);
//        }
//    }
//
//    public static void persistUsers() {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(GAMES_PATH.toFile(), games);
//        } catch (IOException e) {
//            throw new CouldNotWriteUsersException();
//        }
//    }
//}