package ru.itmo.web.lab4.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import ru.itmo.web.lab4.ejb.AreaService;

@Path("/test")
public class TestResource {

    @Inject
    private AreaService areaService;

    @GET
    @Path("/hit")
    public String testHit(
            @QueryParam("x") double x,
            @QueryParam("y") double y,
            @QueryParam("r") double r
    ) {
        boolean result = areaService.checkArea(x, y, r);
        return String.valueOf(result);
    }
}
