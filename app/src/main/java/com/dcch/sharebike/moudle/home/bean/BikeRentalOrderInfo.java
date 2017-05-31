package com.dcch.sharebike.moudle.home.bean;


public class BikeRentalOrderInfo {
    /**
     * carRentalOrderId : 4d33a39bceb740e0a51b392f037ba07f
     * carRentalOrderNo : carrental201703021038301383839
     * userId : 24
     * name : null
     * phone : null
     * nickName : null
     * idCard : null
     * bicycleId : 0
     * organization_ID : 2
     * organization_No : null
     * organization_Name : null
     * bicycleNo : 1000800020
     * tripDist : 0
     * tripTime : 0
     * rideCost : 0
     * carRentalOrderStatus : null
     * carRentalOrderDate : 2017-03-02 10:38:30
     * startTime : null
     * endTime : null
     * resultStatus : 1
     * startPos : 0
     * remark : null
     * returnBicycleDate : null
     */

    private String carRentalOrderId;
    private String carRentalOrderNo;
    private int userId;

    private int bicycleId;

    private String bicycleNo;
    private int tripDist;
    private int tripTime;
    private int rideCost;

    private String carRentalOrderDate;

    private String resultStatus;
    private int startPos;


    public String getCarRentalOrderId() {
        return carRentalOrderId;
    }

    public void setCarRentalOrderId(String carRentalOrderId) {
        this.carRentalOrderId = carRentalOrderId;
    }

    public String getCarRentalOrderNo() {
        return carRentalOrderNo;
    }

    public void setCarRentalOrderNo(String carRentalOrderNo) {
        this.carRentalOrderNo = carRentalOrderNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }









    public int getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(int bicycleId) {
        this.bicycleId = bicycleId;
    }

    public String getBicycleNo() {
        return bicycleNo;
    }

    public void setBicycleNo(String bicycleNo) {
        this.bicycleNo = bicycleNo;
    }

    public int getTripDist() {
        return tripDist;
    }

    public void setTripDist(int tripDist) {
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


    public String getCarRentalOrderDate() {
        return carRentalOrderDate;
    }

    public void setCarRentalOrderDate(String carRentalOrderDate) {
        this.carRentalOrderDate = carRentalOrderDate;
    }


    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

}
