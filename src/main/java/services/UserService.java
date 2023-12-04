package services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



public class UserService {

    /*
THIS HAS NO USE ANYMORE BUT ITS KEPT HERE AS A POINT OF REFERENCE FOR WHAT WAS DONE BEFORE AND AS A HELPER FOR FUTURE PROJECTS
 */

//    public static List<User> users = new ArrayList<>();
//    private static final Path USERS_PATH = FileSystemService.getPathToFile("config", "users.json");
//
//    public static boolean addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
//        checkUserDoesNotAlreadyExist(username);
//        DeveloperService.checkUserDoesNotAlreadyExist(username);
//        users.add(new User(username, encodePassword(username, password), role));
//        persistUsers();
//        return true;
//    }
//
//    public static boolean loadUsersFromFile() throws IOException {
//        if (!Files.exists(USERS_PATH)) {
//            FileUtils.copyURLToFile(UserService.class.getClassLoader().getResource("uzeri.json"), USERS_PATH.toFile());
//        }
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        users = objectMapper.readValue(USERS_PATH.toFile(), new TypeReference<>() {
//        });
//        return true;
//    }
//
//    public static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
//        for (User user : users) {
//            if (Objects.equals(username, user.getUsername()))
//                throw new UsernameAlreadyExistsException(username);
//        }
//    }
//
//    public static void persistUsers() {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(USERS_PATH.toFile(), users);
//        } catch (IOException e) {
//            throw new CouldNotWriteUsersException();
//        }
//    }

    public static boolean login(String username, String password){
        boolean check = false;
        try {

            Connection con = ConnectionService.getConnection();
            Statement check_users = con.createStatement();
            ResultSet users_information = check_users.executeQuery("SELECT username, password, role FROM users");

            while(users_information.next()){
                if(username.equals(users_information.getString(1))
                        && encodePassword(username, password).equals(users_information.getString(2))
                        && users_information.getString(3).equals("Customer")){
                    check = true;
                    con.close();
                    check_users.close();
                    users_information.close();
                    return check;
                } else {
                    check = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\u0000", ""); //to be able to save in JSON format
        // target was "\""
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