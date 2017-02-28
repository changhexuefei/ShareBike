package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class UserBookingBikeInfo {
    /**
     * bookingCarId : 2d49555cb9ad4aa1b14916277fdd400f
     * bookingCarNo : bookingcar201702281018250852938
     * userId : 119
     * name : null
     * phone : null
     * nickName : null
     * idCard : null
     * bicycleId : 0
     * bicycleNo : 1000800058
     * bookingCarDate : 2017-02-28 10:18:25
     * bookingCarStatus : 0
     * resultStatus : 1
     * remark : null
     * longitude : 116.31256476027793
     * latitude : 40.050456963226345
     * address : 北京市北京市海淀区西二旗智学苑12号楼
     */

    private String bookingCarId;
    private String bookingCarNo;
    private int userId;
    private Object name;
    private Object phone;
    private Object nickName;
    private Object idCard;
    private int bicycleId;
    private String bicycleNo;
    private String bookingCarDate;
    private int bookingCarStatus;
    private String resultStatus;
    private Object remark;
    private String longitude;
    private String latitude;
    private String address;

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

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getNickName() {
        return nickName;
    }

    public void setNickName(Object nickName) {
        this.nickName = nickName;
    }

    public Object getIdCard() {
        return idCard;
    }

    public void setIdCard(Object idCard) {
        this.idCard = idCard;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
