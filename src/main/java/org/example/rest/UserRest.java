package org.example.rest;

import org.example.model.*;
import org.example.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRest {

    private final UserService service = UserService.getInstance();

    @GET
    public List<User> getAll() throws Exception {
        return service.getAll();
    }
    
    @GET
    @Path("/{id}")
    public User getById(@PathParam("id") int id) throws Exception {
        return service.getById(id);
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") int id, User u) throws Exception {
        u.setId(id);
        service.update(u);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) throws Exception {
        service.delete(id);
    }
    
    @GET
    @Path("/{id}/assignedTickets")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> getAssignedTickets(@PathParam("id") int id) throws Exception {
        return UserService.getInstance().getAssignedTickets(id);
    }

}
