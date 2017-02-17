package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class BikeInfo {
    public String getBikeAddress() {
        return bikeAddress;
    }

    public BikeInfo(String bikeAddress, String distance) {
        this.bikeAddress = bikeAddress;
        this.distance = distance;
    }

    public void setBikeAddress(String bikeAddress) {
        this.bikeAddress = bikeAddress;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    private String bikeAddress;
    private String distance;


}
