package org.example.service;

import org.example.dao.*;
import org.example.model.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class TicketService {

    private static final TicketService instance = new TicketService();

    private TicketService() {
    }

    public static TicketService getInstance() {
        return instance;
    }

    public void create(Ticket t) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            t.setStatus(TicketStatus.OPEN);
            t.setCreatedAt(LocalDateTime.now());
            
            con.setAutoCommit(false);

            TicketDAO.getInstance().insert(t, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public List<Ticket> getAll() throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return TicketDAO.getInstance().findAll(con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public Ticket getById(int id) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return TicketDAO.getInstance().findById(id, con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public void update(Ticket t) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            
            con.setAutoCommit(false);
            
            TicketDAO.getInstance().update(t, con);
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
            
            TicketDAO.getInstance().delete(id, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public void changeStatus(int ticketId, TicketStatus newStatus, int userId) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            Ticket t = TicketDAO.getInstance().findById(ticketId, con);
            if (t == null) throw new RuntimeException("Ticket not found");

            TicketStatus oldStatus = t.getStatus();
            t.setStatus(newStatus);

            TicketDAO.getInstance().update(t, con);

            StatusLog log = new StatusLog();
            log.setTicketId(ticketId);
            log.setOldStatus(oldStatus);
            log.setNewStatus(newStatus);
            log.setChangedBy(userId);
            log.setChangedAt(LocalDateTime.now());

            StatusLogDAO.getInstance().insert(log, con);

            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public List<Ticket> filterTickets(
            String status,
            String priority,
            String category,
            String keyword,
            LocalDateTime from,
            LocalDateTime to
    ) throws Exception {
        try (Connection con = ResourcesManager.getConnection()) {
            return TicketDAO.getInstance().filterTickets(
                    status,
                    priority,
                    category,
                    keyword,
                    from,
                    to
            );
        }
    }
    
    public void changePriority(int ticketId, Priority priority) throws Exception {
    Connection con = ResourcesManager.getConnection();
    try {
        con.setAutoCommit(false);

        Ticket t = TicketDAO.getInstance().findById(ticketId, con);
        if (t == null) throw new RuntimeException("Ticket not found");

        TicketDAO.getInstance().updatePriority(ticketId, priority, con);

        ResourcesManager.commit(con);
    } catch (Exception e) {
        ResourcesManager.rollback(con);
        throw e;
    } finally {
        ResourcesManager.close(con);
        }
    }

    public void assignTicket(int ticketId, int userId) throws Exception {
    Connection con = ResourcesManager.getConnection();
    try {
        con.setAutoCommit(false);

        Ticket t = TicketDAO.getInstance().findById(ticketId, con);
        if (t == null) throw new RuntimeException("Ticket not found");

        User u = UserDAO.getInstance().findById(userId, con);
        if (u == null) throw new RuntimeException("User not found");

        TicketDAO.getInstance().assignTicket(ticketId, userId, con);

        ResourcesManager.commit(con);
    } catch (Exception e) {
        ResourcesManager.rollback(con);
        throw e;
    } finally {
        ResourcesManager.close(con);
        }
    }

}
