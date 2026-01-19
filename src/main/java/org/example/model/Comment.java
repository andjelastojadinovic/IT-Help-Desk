package org.example.model;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int ticketId;
    private int authorId;
    private String text;
    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(int ticketId, int authorId, String text) {
        this.ticketId = ticketId;
        this.authorId = authorId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", authorId=" + authorId +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}