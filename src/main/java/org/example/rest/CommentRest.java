package org.example.rest;

import org.example.model.Comment;
import org.example.service.CommentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentRest {

     private final CommentService service = CommentService.getInstance();

    @GET
    public List<Comment> getAll() throws Exception {
        return service.getAll();
    }

    @GET
    @Path("/tickets/{ticketId}")
    public List<Comment> getByTicket(@PathParam("ticketId") int ticketId) throws Exception {
        return service.getByTicket(ticketId);
    }

    @POST
    public void add(Comment c) throws Exception {
        service.add(c);
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") int id, Comment c) throws Exception {
        c.setId(id);
        service.update(c);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) throws Exception {
        service.delete(id);
    }
}
