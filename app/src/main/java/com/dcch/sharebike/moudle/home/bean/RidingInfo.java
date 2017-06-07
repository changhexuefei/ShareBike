package com.dcch.sharebike.moudle.home.bean;

import java.io.Serializable;


public class RidingInfo implements Serializable {

    /**
     * carRentalOrderId : 786c5908ca1941ee9919cb9e8551697f
     * tripDist : 0.008730947
     * tripTime : 0
     * rideCost : 1
     * status : 0
     * calorie : 0.25319746
     * resultStatus : 1
     * amount : 0
     * bicycleNo : null
     * couponno : null
     * couponAmount : 0
     * orderCast : 0
     * finalCast : 0
     */
    //{"resultStatus":"1","carRentalInfo":{"amount":0,"bicycleNo":"","calorie":0,"carRentalOrderId":"c109794e26e640c7bf1c37cc75e99a0b","couponAmount":0,"couponno":"nocoupon","finalCast":0.5,"orderCast":0,"resultStatus":"","rideCost":0.5,"status":0,"tripDist":0,"tripTime":1}}

    private String carRentalOrderId;
    private double tripDist;
    private int tripTime;
    private float rideCost;
    private int status;
    private double calorie;
    private String resultStatus;
    private float amount;
    private Object couponno;
    private float couponAmount;
    private float orderCast;
    private float finalCast;

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

    public float getRideCost() {
        return rideCost;
    }

    public void setRideCost(float rideCost) {
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Object getCouponno() {
        return couponno;
    }

    public void setCouponno(Object couponno) {
        this.couponno = couponno;
    }

    public float getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(float couponAmount) {
        this.couponAmount = couponAmount;
    }

    public float getOrderCast() {
        return orderCast;
    }

    public void setOrderCast(float orderCast) {
        this.orderCast = orderCast;
    }

    public float getFinalCast() {
        return finalCast;
    }

    public void setFinalCast(float finalCast) {
        this.finalCast = finalCast;
    }
}
