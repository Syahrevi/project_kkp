package harahap;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {

    public static void connect() {
        // connection string
        var url = "jdbc:sqlite:harahap.db";

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
        System.out.println(System.getProperty("user.dir"));
    }
}