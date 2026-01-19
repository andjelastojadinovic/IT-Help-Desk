package org.example.service;

import org.example.dao.ResourcesManager;
import org.example.dao.*;
import org.example.model.*;

import java.security.MessageDigest;
import java.sql.Connection;
import java.util.List;

public class UserService {

    private static final UserService instance = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return instance;
    }

    public void register(User user) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            if (UserDAO.getInstance().findByUsername(user.getUsername(), con) != null)
                throw new RuntimeException("Username already exists");

            user.setPassword(hash(user.getPassword()));
            UserDAO.getInstance().insert(user, con);

            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public User login(String username, String password) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            User u = UserDAO.getInstance().findByUsername(username, con);
            if (u == null || !u.getPassword().equals(hash(password)))
                throw new RuntimeException("Invalid credentials");

            return u;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public List<User> getAll() throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return UserDAO.getInstance().findAll(con);
        } finally {
            ResourcesManager.close(con);
        }
    }
    
    public User getById(int id) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return UserDAO.getInstance().findById(id, con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public void update(User user) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            
            con.setAutoCommit(false);

            UserDAO.getInstance().update(user, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public void delete(int id) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            
            con.setAutoCommit(false);

            UserDAO.getInstance().delete(id, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    private String hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
    
    public List<Ticket> getAssignedTickets(int userId) throws Exception {
    Connection con = ResourcesManager.getConnection();
    try {
        return TicketDAO.getInstance().findByAssignedUser(userId, con);
    } finally {
        ResourcesManager.close(con);
    }
}


}
