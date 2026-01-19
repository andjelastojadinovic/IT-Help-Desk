package org.example.rest;

import org.example.model.StatusLog;
import org.example.service.StatusLogService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
public class StatusLogRest {

    private final StatusLogService service = StatusLogService.getInstance();

    @GET
    public List<StatusLog> getAll() throws Exception {
        return service.getAll();
    }

    @GET
    @Path("/{id}")
    public StatusLog getById(@PathParam("id") int id) throws Exception {
        return service.getById(id);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) throws Exception {
        service.delete(id);
    }
}
