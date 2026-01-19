package org.example.rest;

import org.example.model.User;
import org.example.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthRest {

    private final UserService userService = UserService.getInstance();

    @POST
    @Path("/login")
    public User login(User u) throws Exception {
        return userService.login(u.getUsername(), u.getPassword());
    }

    @POST
    @Path("/register")
    public void register(User u) throws Exception {
        userService.register(u);
    }
}
