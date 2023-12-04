package services;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionService {
    private static DataSource dataSource;

    public static void initializeConnectionPool() {
        BasicDataSource basicDataSource = new BasicDataSource();
        String url = ConfigReader.getURL();
        String user = ConfigReader.getUser();
        String password = ConfigReader.getPassword();

        basicDataSource.setUrl(url);
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxTotal(20);
        basicDataSource.setDefaultAutoCommit(false);

        dataSource = basicDataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
