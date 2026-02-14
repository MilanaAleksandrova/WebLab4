package ru.itmo.web.lab4.rest;

import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    private static final String SESSION_USER_ID_KEY = "uid";

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        String path = normalizePath(requestContext.getUriInfo().getPath());
        if (isPublicPath(path)) {
            return;
        }

        if (request.getSession(false) == null) {
            abortUnauthorized(requestContext);
            return;
        }

        Object userId = request.getSession(false).getAttribute(SESSION_USER_ID_KEY);
        if (userId == null) {
            abortUnauthorized(requestContext);
        }
    }

    private String normalizePath(String rawPath) {
        if (rawPath == null) {
            return "";
        }
        String path = rawPath;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    private boolean isPublicPath(String path) {
        return "ping".equals(path)
                || "auth/login".equals(path)
                || "auth/register".equals(path);
    }

    private void abortUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(Map.of("error", "Unauthorized"))
                        .build()
        );
    }
}
