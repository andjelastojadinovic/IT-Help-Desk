package org.example.dao;

import org.example.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    private static final CommentDAO instance = new CommentDAO();

    private CommentDAO() {
    }

    public static CommentDAO getInstance() {
        return instance;
    }
    

    // INSERT
    public void insert(Comment c, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("INSERT INTO comments(ticket_id, author_id, text, created_at)" +
                "VALUES(?,?,?,?)");
        ps.setInt(1, c.getTicketId());
        ps.setInt(2, c.getAuthorId());
        ps.setString(3, c.getText());
        ps.setTimestamp(4, Timestamp.valueOf(c.getCreatedAt()));
        ps.executeUpdate();
    }

    // FIND BY ID
    public Comment findById(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM comments WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) return null;
        return map(rs);
    }

    // FIND ALL
    public List<Comment> findAll(Connection con) throws Exception {
        List<Comment> list = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM comments");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) list.add(map(rs));
        return list;
    }

    // UPDATE
    public void update(Comment c, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "UPDATE comments SET text=? WHERE id=?");
        ps.setString(1, c.getText());
        ps.setInt(2, c.getId());
        ps.executeUpdate();
    }

    // DELETE
    public void delete(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM comments WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // DODATNO
    public List<Comment> findByTicket(int ticketId, Connection con) throws Exception {
        List<Comment> list = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM comments WHERE ticket_id=?");
        ps.setInt(1, ticketId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) list.add(map(rs));
        return list;
    }

    private Comment map(ResultSet rs) throws Exception {
        Comment c = new Comment();
        c.setId(rs.getInt("id"));
        c.setTicketId(rs.getInt("ticket_id"));
        c.setAuthorId(rs.getInt("author_id"));
        c.setText(rs.getString("text"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return c;
    }
}
