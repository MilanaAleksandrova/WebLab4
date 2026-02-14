package ru.itmo.web.lab4.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ru.itmo.web.lab4.dto.ShotDto;
import ru.itmo.web.lab4.ejb.ShotService;
import ru.itmo.web.lab4.entity.Shot;

import java.util.List;

@Path("/shots")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShotResource {

    @Inject
    private ShotService shotService;

    @POST
    public Shot createShot(ShotDto request) {
        return shotService.createShot(
                request.getX(),
                request.getY(),
                request.getR()
        );
    }

    @GET
    public List<Shot> getAllShots() {
        return shotService.getAllShots();
    }
}
