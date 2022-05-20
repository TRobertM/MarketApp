package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.CouldNotWriteUsersException;
import exception.UsernameAlreadyExistsException;
import model.Developer;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;


public class DeveloperService {

    public static List<Developer> developers = new ArrayList<>();
    private static final Path DEVELOPERS_PATH = FileSystemService.getPathToFile("config", "developers.json");

    public static void addUser(String username, String password) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        UserService.checkUserDoesNotAlreadyExist(username);
        developers.add(new Developer(username, encodePassword(username, password)));
        persistUsers();
    }

    public static void loadUsersFromFile() throws IOException {
        if (!Files.exists(DEVELOPERS_PATH)) {
            FileUtils.copyURLToFile(UserService.class.getClassLoader().getResource("developerz.json"), DEVELOPERS_PATH.toFile());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        developers = objectMapper.readValue(DEVELOPERS_PATH.toFile(), new TypeReference<>() {
        });
    }

    public static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (Developer developer : developers) {
            if (Objects.equals(username, developer.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    private static void persistUsers() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(DEVELOPERS_PATH.toFile(), developers);
        } catch (IOException e) {
            throw new CouldNotWriteUsersException();
        }
    }

    public static boolean login(String username, String password){
        for(Developer developer : developers){
            if(username.equals(developer.getUsername()) && encodePassword(username, password).equals(developer.getPassword())){
                return true;
            }
        }
        return false;
    }

    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
}