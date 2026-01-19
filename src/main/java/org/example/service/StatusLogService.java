package org.example.service;

import org.example.dao.ResourcesManager;
import org.example.dao.StatusLogDAO;
import org.example.model.StatusLog;

import java.sql.Connection;
import java.util.List;

public class StatusLogService {

    private static final StatusLogService instance = new StatusLogService();

    private StatusLogService() {
    }

    public static StatusLogService getInstance() {
        return instance;
    }

    public List<StatusLog> getAll() throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return StatusLogDAO.getInstance().findAll(con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public StatusLog getById(int id) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return StatusLogDAO.getInstance().findById(id, con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public void delete(int id) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            con.setAutoCommit(false);
            StatusLogDAO.getInstance().delete(id, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }
    
    public List<StatusLog> getByTicket(int ticketId) throws Exception {
        Connection con = ResourcesManager.getConnection();
        return StatusLogDAO.getInstance().findByTicketId(ticketId, con);
    }
}
