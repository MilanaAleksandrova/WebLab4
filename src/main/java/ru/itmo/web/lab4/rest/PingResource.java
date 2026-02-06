package ru.itmo.web.lab4.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/ping")
public class PingResource {

    @GET
    public String ping() {
        return "ok";
    }
}
