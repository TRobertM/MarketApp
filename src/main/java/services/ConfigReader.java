package services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class ConfigReader {
    private static Properties properties;

    static{
        properties = new Properties();
        try(InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")){
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getURL(){
        return properties.getProperty("url");
    }

    public static String getUser(){
        return properties.getProperty("user");
    }

    public static String getPassword(){
        return properties.getProperty("password");
    }
}
