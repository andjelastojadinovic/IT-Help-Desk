package org.example.dao;

import org.example.model.StatusLog;
import org.example.model.TicketStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusLogDAO {
    
    private static final StatusLogDAO instance = new StatusLogDAO();

    private StatusLogDAO() {
    }

    public static StatusLogDAO getInstance() {
        return instance;
    }

    public void insert(StatusLog s, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("INSERT INTO status_logs(ticket_id, old_status, new_status, changed_by, changed_at)" +
                "VALUES(?,?,?,?,?)");
        ps.setInt(1, s.getTicketId());
        ps.setString(2, s.getOldStatus().name());
        ps.setString(3, s.getNewStatus().name());
        ps.setInt(4, s.getChangedBy());
        ps.setTimestamp(5, Timestamp.valueOf(s.getChangedAt()));
        ps.executeUpdate();
    }

    public StatusLog findById(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM status_logs WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;

        StatusLog s = new StatusLog();
        s.setId(id);
        s.setTicketId(rs.getInt("ticket_id"));
        s.setOldStatus(TicketStatus.valueOf(rs.getString("old_status")));
        s.setNewStatus(TicketStatus.valueOf(rs.getString("new_status")));
        s.setChangedBy(rs.getInt("changed_by"));
        s.setChangedAt(rs.getTimestamp("changed_at").toLocalDateTime());
        return s;
    }

    public List<StatusLog> findAll(Connection con) throws Exception {
        List<StatusLog> list = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM status_logs");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) list.add(findById(rs.getInt("id"), con));
        return list;
    }

    public void delete(int id, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "DELETE FROM status_logs WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }
    
    public List<StatusLog> findByTicketId(int ticketId, Connection con) throws Exception {
    List<StatusLog> logs = new ArrayList<>();

    String sql = "SELECT * FROM status_logs WHERE ticket_id = ? ORDER BY changed_at";

    try (PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, ticketId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            logs.add(map(rs));
        }
    }
    return logs;
    }
    
    private StatusLog map(ResultSet rs) throws SQLException {
    StatusLog log = new StatusLog();

    log.setId(rs.getInt("id"));
    log.setTicketId(rs.getInt("ticket_id"));
    log.setOldStatus(TicketStatus.valueOf(rs.getString("old_status")));
    log.setNewStatus(TicketStatus.valueOf(rs.getString("new_status")));
    log.setChangedBy(rs.getInt("changed_by"));
    Timestamp ts = rs.getTimestamp("changed_at");
    if (ts != null) {
        log.setChangedAt(ts.toLocalDateTime());
    }

    return log;
}



    
}
