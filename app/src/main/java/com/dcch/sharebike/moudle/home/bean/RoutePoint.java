package com.dcch.sharebike.moudle.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class RoutePoint implements Serializable {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
