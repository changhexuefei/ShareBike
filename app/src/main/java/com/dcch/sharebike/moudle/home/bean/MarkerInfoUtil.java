package com.dcch.sharebike.moudle.home.bean;

import java.io.Serializable;

/**
 * Created by gao on 2017/2/12.
 */

public class MarkerInfoUtil implements Serializable {
    private static final long serialVersionUID = 8633299996744734593L;
    private double latitude;//纬度
    private double longitude;//经度

    public MarkerInfoUtil() {
    }

    public MarkerInfoUtil(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }




}
