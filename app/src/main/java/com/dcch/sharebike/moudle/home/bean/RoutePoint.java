package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class RoutePoint {
    private double routeLat;
    private double routeLng;
    public double getRouteLat() {
        return routeLat;
    }

    public void setRouteLat(double routeLat) {
        this.routeLat = routeLat;
    }

    public double getRouteLng() {
        return routeLng;
    }

    public void setRouteLng(double routeLng) {
        this.routeLng = routeLng;
    }

    @Override
    public String toString() {
        return "RoutePoint{" +
                "routeLat=" + routeLat +
                ", routeLng=" + routeLng +
                '}';
    }
}
