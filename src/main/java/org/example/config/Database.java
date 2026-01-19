package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/it_helpdesk?useSSL=false&serverTimezone=UTC",
                "root",
                ""
        );
    }
}
