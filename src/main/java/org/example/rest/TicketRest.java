package org.example.rest;

import org.example.model.*;
import org.example.service.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketRest {

    private final TicketService service = TicketService.getInstance();

    @GET
    public List<Ticket> getAll() throws Exception {
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    public Ticket getById(@PathParam("id") int id) throws Exception {
        return service.getById(id);
    }

    @POST
    public void create(Ticket t) throws Exception {
        service.create(t);
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") int id, Ticket t) throws Exception {
        t.setId(id);
        service.update(t);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) throws Exception {
        service.delete(id);
    }

    @PUT
    @Path("/{id}/status")
    public void changeStatus(@PathParam("id") int id,
                             @QueryParam("status") TicketStatus status,
                             @QueryParam("userId") int userId) throws Exception {
        service.changeStatus(id, status, userId);
    }

    @GET
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> filterTickets(
            @QueryParam("status") String status,
            @QueryParam("priority") String priority,
            @QueryParam("category") String category,
            @QueryParam("keyword") String keyword,
            @QueryParam("fromDate") String fromDate,
            @QueryParam("toDate") String toDate
    ) throws Exception {
        LocalDateTime from = null;
        LocalDateTime to = null;

        if (fromDate != null) {
            from = LocalDateTime.parse(fromDate);
        }
        if (toDate != null) {
            to = LocalDateTime.parse(toDate);
        }

        return service.filterTickets(
                status,
                priority,
                category,
                keyword,
                from,
                to
        );
    }
    
    @GET
    @Path("/{id}/logs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatusLog> getTicketLogs(@PathParam("id") int id) throws Exception {
        return StatusLogService.getInstance().getByTicket(id);
    }

    @PUT
    @Path("/{id}/priority")
    public void changePriority(
            @PathParam("id") int id,
            @QueryParam("priority") String priority
    ) throws Exception {
        service.changePriority(id, Priority.valueOf(priority));
    }
    
    @PATCH
    @Path("/{id}/assign")
    public void assignTicket(
            @PathParam("id") int id,
            @QueryParam("userId") int userId
    ) throws Exception {
        service.assignTicket(id, userId);
    }



}
