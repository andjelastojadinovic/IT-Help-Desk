package org.example.dao;

import org.example.model.Role;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    private static final UserDAO instance = new UserDAO();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return instance;
    }
    
    public void insert(User u, Connection con) throws Exception {
        String sql = "INSERT INTO users(first_name, last_name, username, password, email, role)"+
                "VALUES(?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, u.getFirstName());
        ps.setString(2, u.getLastName());
        ps.setString(3, u.getUsername());
        ps.setString(4, u.getPassword());
        ps.setString(5, u.getEmail());
        ps.setString(6, u.getRole().name());
        ps.executeUpdate();
    }

    public User findById(int id, Connection con) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;

        User u = map(rs);
        return u;
    }

    public List<User> findAll(Connection con) throws Exception {
        List<User> list = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM users");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(map(rs));
        }
        return list;
    }

    public void update(User u, Connection con) throws Exception {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, role=?" +
                "WHERE id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, u.getFirstName());
        ps.setString(2, u.getLastName());
        ps.setString(3, u.getEmail());
        ps.setString(4, u.getRole().name());
        ps.setInt(5, u.getId());
        ps.executeUpdate();
    }

    public void delete(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM users WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public User findByUsername(String username, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username=?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) return null;
        return map(rs);
    }

    private User map(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setEmail(rs.getString("email"));
        u.setRole(Role.valueOf(rs.getString("role")));
        return u;
    }
}
