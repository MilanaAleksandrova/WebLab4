package ru.itmo.web.lab4.rest;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.itmo.web.lab4.dto.AuthRequest;
import ru.itmo.web.lab4.dto.AuthResponse;
import ru.itmo.web.lab4.ejb.AuthService;
import ru.itmo.web.lab4.entity.User;

import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final String SESSION_USER_ID_KEY = "uid";

    @Inject
    private AuthService authService;

    @POST
    @Path("/register")
    public Response register(AuthRequest request, @Context HttpServletRequest httpRequest) {
        try {
            User user = authService.register(request.getUsername(), request.getPassword());
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(SESSION_USER_ID_KEY, user.getId());
            return Response.status(Response.Status.CREATED)
                    .entity(toAuthResponse(user))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(AuthRequest request, @Context HttpServletRequest httpRequest) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(SESSION_USER_ID_KEY, user.getId());
            return Response.ok(toAuthResponse(user)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/me")
    public Response me(@Context HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Object userIdRaw = session.getAttribute(SESSION_USER_ID_KEY);
        if (!(userIdRaw instanceof Number number)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Long userId = number.longValue();

        User user = authService.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(toAuthResponse(user)).build();
    }

    private AuthResponse toAuthResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return response;
    }
}
