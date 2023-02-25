

/*
THIS HAS NO USE ANYMORE BUT ITS KEPT HERE AS A POINT OF REFERENCE FOR WHAT WAS DONE BEFORE AND AS A HELPER FOR FUTURE PROJECTS
 */

//package services;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class FileSystemService {
//    private static final String APPLICATION_FOLDER = ".MarketApp";
//    private static final String USER_FOLDER = System.getProperty("user.home");
//    public static final Path APPLICATION_HOME_PATH = Paths.get(USER_FOLDER, APPLICATION_FOLDER);
//
//    public static Path getPathToFile(String... path) {
//        return APPLICATION_HOME_PATH.resolve(Paths.get(".", path));
//    }
//}