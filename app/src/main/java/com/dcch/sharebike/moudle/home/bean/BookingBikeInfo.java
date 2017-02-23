package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class BookingBikeInfo {


    /**
     * bookingCarId : 4482b048f12948c08376908f1d51868b
     * bookingCarNo : bookingcar201702231703208408779
     * userId : 32
     * name : null
     * phone : null
     * nickName : null
     * idCard : null
     * bicycleId : 46
     * bicycleNo : null
     * bookingCarDate : 2017-02-23 17:03:20
     * bookingCarStatus : 0
     * resultStatus : 1
     * remark : null
     */

    private String bookingCarId;
    private String bookingCarNo;
    private int userId;
    private Object name;
    private Object phone;
    private Object nickName;
    private Object idCard;
    private int bicycleId;
    private Object bicycleNo;
    private String bookingCarDate;
    private int bookingCarStatus;
    private String resultStatus;
    private Object remark;

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

    public Object getBicycleNo() {
        return bicycleNo;
    }

    public void setBicycleNo(Object bicycleNo) {
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
}
