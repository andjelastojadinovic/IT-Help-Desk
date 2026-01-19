package org.example.service;

import org.example.dao.CommentDAO;
import org.example.dao.ResourcesManager;
import org.example.model.Comment;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class CommentService {

    private static final CommentService instance = new CommentService();

    private CommentService() {
    }

    public static CommentService getInstance() {
        return instance;
    }

    public void add(Comment c) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            c.setCreatedAt(LocalDateTime.now());
            
            con.setAutoCommit(false);
            
            CommentDAO.getInstance().insert(c, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }

    public List<Comment> getAll() throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return CommentDAO.getInstance().findAll(con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public List<Comment> getByTicket(int ticketId) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            return CommentDAO.getInstance().findByTicket(ticketId, con);
        } finally {
            ResourcesManager.close(con);
        }
    }

    public void update(Comment c) throws Exception {
        Connection con = ResourcesManager.getConnection();
        try {
            con.setAutoCommit(false);
            
            CommentDAO.getInstance().update(c, con);
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
            
            CommentDAO.getInstance().delete(id, con);
            ResourcesManager.commit(con);
        } catch (Exception e) {
            ResourcesManager.rollback(con);
            throw e;
        } finally {
            ResourcesManager.close(con);
        }
    }
}
