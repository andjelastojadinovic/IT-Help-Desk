package org.example.dao;

import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.config.*;
import java.time.LocalDateTime;

public class TicketDAO {
    
    private static final TicketDAO instance = new TicketDAO();

    private TicketDAO() {
    }

    public static TicketDAO getInstance() {
        return instance;
    }
    

    public void insert(Ticket t, Connection con) throws Exception {
        String sql = "INSERT INTO tickets(title, description, status, priority, " +
        "category, created_at, created_by, assigned_to) " +
        "VALUES(?,?,?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, t.getTitle());
        ps.setString(2, t.getDescription());
        ps.setString(3, t.getStatus().name());
        ps.setString(4, t.getPriority().name());
        ps.setString(5, t.getCategory());
        ps.setTimestamp(6, Timestamp.valueOf(t.getCreatedAt()));
        ps.setInt(7, t.getCreatedBy());
        ps.setObject(8, t.getAssignedTo());
        ps.executeUpdate();
    }

    public Ticket findById(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM tickets WHERE id=?");
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;

        return map(rs);
    }

    public List<Ticket> findAll(Connection con) throws Exception {
        List<Ticket> list = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM tickets");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) list.add(map(rs));
        return list;
    }

    public void update(Ticket t, Connection con) throws Exception {
        String sql = "UPDATE tickets SET title=?, description=?,status=?, priority=?," +
                "category=?, assigned_to=? WHERE id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, t.getTitle());
        ps.setString(2, t.getDescription());
        ps.setString(3, t.getStatus().name());
        ps.setString(4, t.getPriority().name());
        ps.setString(5, t.getCategory());
        ps.setObject(6, t.getAssignedTo());
        ps.setInt(7, t.getId());
        ps.executeUpdate();
    }

    public void delete(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM tickets WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    private Ticket map(ResultSet rs) throws Exception {
        Ticket t = new Ticket();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setStatus(TicketStatus.valueOf(rs.getString("status")));
        t.setPriority(Priority.valueOf(rs.getString("priority")));
        t.setCategory(rs.getString("category"));
        t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        t.setCreatedBy(rs.getInt("created_by"));
        t.setAssignedTo((Integer) rs.getObject("assigned_to"));
        return t;
    }

    public List<Ticket> filterTickets(
            String status,
            String priority,
            String category,
            String keyword,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        List<Ticket> tickets = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM tickets WHERE 1=1"
        );

        if (status != null) {
            sql.append(" AND status = ?");
        }
        if (priority != null) {
            sql.append(" AND priority = ?");
        }
        if (category != null) {
            sql.append(" AND category = ?");
        }
        if (keyword != null) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
        }
        if (fromDate != null) {
            sql.append(" AND created_at >= ?");
        }
        if (toDate != null) {
            sql.append(" AND created_at <= ?");
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;

            if (status != null) ps.setString(i++, status);
            if (priority != null) ps.setString(i++, priority);
            if (category != null) ps.setString(i++, category);
            if (keyword != null) {
                ps.setString(i++, "%" + keyword + "%");
                ps.setString(i++, "%" + keyword + "%");
            }
            if (fromDate != null)
                ps.setTimestamp(i++, Timestamp.valueOf(fromDate));
            if (toDate != null)
                ps.setTimestamp(i++, Timestamp.valueOf(toDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tickets.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;
    }
    
    public List<Ticket> findByAssignedUser(int userId, Connection con) throws Exception {
    List<Ticket> tickets = new ArrayList<>();

    String sql = "SELECT * FROM tickets WHERE assigned_to = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            tickets.add(map(rs));
        }
    }
        return tickets;
    }
    
    public void updatePriority(int ticketId, Priority priority, Connection con) throws Exception {
    String sql = "UPDATE tickets SET priority=? WHERE id=?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, priority.name());
    ps.setInt(2, ticketId);
    ps.executeUpdate();
    }
    
    public void assignTicket(int ticketId, int userId, Connection con) throws Exception {
    String sql = "UPDATE tickets SET assigned_to=? WHERE id=?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setInt(1, userId);
    ps.setInt(2, ticketId);
    ps.executeUpdate();
    }

}
