package com.dcch.sharebike.moudle.home.bean;

import java.io.Serializable;


public class BikeInfo implements Serializable {


    /**
     * bicycleId : 2
     * bicycleNo : 1000800002
     * bicycleName : null
     * bicycleTypeId : null
     * unitPrice : 0
     * price : 0
     * manufacturer : null
     * organization_ID : 0
     * organization_Name : null
     * address : 北京市上地五街
     * usestate : 0
     * anomaly : 0
     * reservestate : 0
     * delflag : null
     * createTime : null
     * releaseTime : null
     * userId : null
     * lockRemark : null
     * systemTime : 2017-01-16 15:14:08.0
     * bicycletime : null
     * sleephour : null
     * positiontype : null
     * locknum : 0
     * electricity : 0
     * online : null
     * longitude : 116.31316476027793
     * latitude : 40.048456963226345
     * minLat : null
     * maxLat : null
     * minLng : null
     * maxLng : null
     */

    private int bicycleId;
    private String bicycleNo;
    private float unitPrice;
    private String price;
    private String address;
    private double longitude;
    private double latitude;


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

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
