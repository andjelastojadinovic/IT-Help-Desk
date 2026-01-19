package org.example.dao;

import org.example.config.Database;
import java.sql.Connection;

public class ResourcesManager {

    public static Connection getConnection() throws Exception {
        Connection con = Database.getConnection();
        con.setAutoCommit(false);
        return con;
    }

    public static void commit(Connection con) {
        try {
            if (con != null) con.commit();
        } catch (Exception ignored) {}
    }

    public static void rollback(Connection con) {
        try {
            if (con != null) con.rollback();
        } catch (Exception ignored) {}
    }

    public static void close(Connection con) {
        try {
            if (con != null) con.close();
        } catch (Exception ignored) {}
    }
}
