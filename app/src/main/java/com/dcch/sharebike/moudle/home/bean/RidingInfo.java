package com.dcch.sharebike.moudle.home.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class RidingInfo implements Serializable {

    /**
     * carRentalOrderId : 4a759fc76b5949b8b3bd3dfce7b31663
     * tripDist : 0.09788349
     * tripTime : 1
     * rideCost : 1
     * status : 0
     * calorie : 2.8386214
     * resultStatus : 1
     */

    private String carRentalOrderId;
    private double tripDist;
    private int tripTime;
    private int rideCost;
    private int status;
    private double calorie;
    private String resultStatus;

    public String getCarRentalOrderId() {
        return carRentalOrderId;
    }

    public void setCarRentalOrderId(String carRentalOrderId) {
        this.carRentalOrderId = carRentalOrderId;
    }

    public double getTripDist() {
        return tripDist;
    }

    public void setTripDist(double tripDist) {
        this.tripDist = tripDist;
    }

    public int getTripTime() {
        return tripTime;
    }

    public void setTripTime(int tripTime) {
        this.tripTime = tripTime;
    }

    public int getRideCost() {
        return rideCost;
    }

    public void setRideCost(int rideCost) {
        this.rideCost = rideCost;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}
