package com.dcch.sharebike.moudle.home.bean;


public class BookingBikeInfo {


    /**
     * bookingCarId : 728581a0c1284526b4d4135634462244
     * bookingCarNo : bookingcar201703021651215615796
     * userId : 34
     * name : null
     * phone : null
     * nickName : null
     * idCard : null
     * bicycleId : 0
     * bicycleNo : 1000800053
     * bookingCarDate : 2017-03-02 16:51:21
     * bookingCarStatus : 0
     * resultStatus : 1
     * remark : null
     * longitude : null
     * latitude : null
     * address : null
     */
    private String bookingCarId;
    private String bookingCarNo;
    private int userId;
    private int bicycleId;
    private String bicycleNo;
    private String bookingCarDate;
    private int bookingCarStatus;
    private String resultStatus;
    private Object remark;
    private String longitude;
    private String latitude;
    private Object address;

    public String getBookingCarId() {
        return bookingCarId;
    }

    public void setBookingCarId(String bookingCarId) {
        this.bookingCarId = bookingCarId;
    }

    public String getBookingCarNo() {
        return bookingCarNo;
    }

    public void setBookingCarNo(String bookingCarNo) {
        this.bookingCarNo = bookingCarNo;
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

    public String getBookingCarDate() {
        return bookingCarDate;
    }

    public void setBookingCarDate(String bookingCarDate) {
        this.bookingCarDate = bookingCarDate;
    }

    public int getBookingCarStatus() {
        return bookingCarStatus;
    }

    public void setBookingCarStatus(int bookingCarStatus) {
        this.bookingCarStatus = bookingCarStatus;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }
}
