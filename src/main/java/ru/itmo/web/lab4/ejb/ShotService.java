package ru.itmo.web.lab4.ejb;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.itmo.web.lab4.entity.Shot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ShotService {

    @PersistenceContext(unitName = "lab4PU")
    private EntityManager em;

    @Inject
    private AreaService areaService;

    public Shot createShot(double doubleX, double doubleY, double doubleR) {

        BigDecimal x = BigDecimal.valueOf(doubleX);
        BigDecimal y = BigDecimal.valueOf(doubleY);
        BigDecimal r = BigDecimal.valueOf(doubleR);

        boolean isHit = areaService.checkArea(x, y, r);

        Shot shot = new Shot(
                x,
                y,
                r,
                isHit,
                LocalDateTime.now()
        );

        em.persist(shot);

        return shot;
    }

    public List<Shot> getAllShots() {
        return em.createQuery("SELECT s FROM Shot s ORDER BY s.id DESC", Shot.class)
                .getResultList();
    }


}
