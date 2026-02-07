package ru.itmo.web.lab4.ejb;

import jakarta.ejb.Stateless;

import java.math.BigDecimal;

@Stateless
public class AreaService {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    public boolean checkArea(double doubleX, double doubleY, double doubleR){
        BigDecimal x = BigDecimal.valueOf(doubleX);
        BigDecimal y = BigDecimal.valueOf(doubleY);
        BigDecimal r = BigDecimal.valueOf(Math.abs(doubleR));

        boolean isHit = checkTriangle(x, y, r) ||
                checkRectangle(x, y, r) ||
                checkCircle(x, y, r);

        if (doubleR < 0){
            isHit = !isHit;
        }

        return isHit;
    }

    private boolean checkTriangle(BigDecimal x, BigDecimal y, BigDecimal r){
        //2 четверть, x <= 0, y >= 0
        boolean isSecondQuarter = x.compareTo(BigDecimal.ZERO) <= 0 && y.compareTo(BigDecimal.ZERO) >= 0;
        //y <= 2x + R
        return isSecondQuarter && y.compareTo(x.multiply(BigDecimal.valueOf(2)).add(r)) <= 0;
    }

    private boolean checkRectangle(BigDecimal x, BigDecimal y, BigDecimal r){
        //4 четверть, x >= 0, y <= 0
        boolean isFourthQuarter = x.compareTo(ZERO) >= 0 && y.compareTo(ZERO) <= 0;
        //y >= -R/2 и x <= R
        BigDecimal halfR = r.divide(TWO);
        return isFourthQuarter && ((y.compareTo(halfR.negate()) >= 0) && (x.compareTo(r) <= 0));
    }

    private boolean checkCircle(BigDecimal x, BigDecimal y, BigDecimal r){
        //3 четверть, x <= 0, y <= 0
        boolean isThirdQuarter = x.compareTo(ZERO) <= 0 && y.compareTo(ZERO) <= 0;
        //x^2 + y^2 <= (r/2)^2
        BigDecimal halfR = r.divide(TWO);
        return isThirdQuarter && x.multiply(x).add(y.multiply(y)).compareTo(halfR.multiply(halfR)) <= 0;
    }

}
