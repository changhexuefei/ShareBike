package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class JourneyInfo {

    /**
     * resultStatus : 1
     * carrOrders : [{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"091700001","calorie":0.23158978,"carRentalOrderDate":"2017-06-02 13:13:25","carRentalOrderId":"d9197cfa49534183b2650cfd8a3aab83","carRentalOrderNo":"carrental201706021313252363124","carRentalOrderStatus":"","couponno":"","endTime":"","idCard":"","integral":0,"lockRemark":"","name":"123456","nickName":"测试","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1.5,"startPos":0,"startTime":"","tripDist":0.01,"tripTime":84,"userAddress":"","userAge":0,"userId":0,"userImage":"/userImage/201705241456082709989one.jpg","userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"091700001","calorie":1.9217808,"carRentalOrderDate":"2017-06-01 22:04:42","carRentalOrderId":"d233d2cc4bc348b6b2874e735ad8719c","carRentalOrderNo":"carrental201706012204424668449","carRentalOrderStatus":"","couponno":"","endTime":"","idCard":"","integral":0,"lockRemark":"","name":"123456","nickName":"测试","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":11,"startPos":0,"startTime":"","tripDist":0.07,"tripTime":643,"userAddress":"","userAge":0,"userId":0,"userImage":"/userImage/201705241456082709989one.jpg","userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"091700001","calorie":36.569004,"carRentalOrderDate":"2017-06-01 19:22:12","carRentalOrderId":"bb2712af583f465ebf5b524132fe0882","carRentalOrderNo":"carrental201706011922120524406","carRentalOrderStatus":"","couponno":"","endTime":"","idCard":"","integral":0,"lockRemark":"","name":"123456","nickName":"测试","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":0.5,"startPos":0,"startTime":"","tripDist":1.26,"tripTime":13,"userAddress":"","userAge":0,"userId":0,"userImage":"/userImage/201705241456082709989one.jpg","userSex":0}]
     */

    private String resultStatus;
    private List<CarrOrdersBean> carrOrders;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<CarrOrdersBean> getCarrOrders() {
        return carrOrders;
    }

    public void setCarrOrders(List<CarrOrdersBean> carrOrders) {
        this.carrOrders = carrOrders;
    }

    public static class CarrOrdersBean {
        /**
         * aggregateAmount : 0
         * bicycleId : 0
         * bicycleNo : 091700001
         * calorie : 0.23158978
         * carRentalOrderDate : 2017-06-02 13:13:25
         * carRentalOrderId : d9197cfa49534183b2650cfd8a3aab83
         * carRentalOrderNo : carrental201706021313252363124
         * carRentalOrderStatus :
         * couponno :
         * endTime :
         * idCard :
         * integral : 0
         * lockRemark :
         * name : 123456
         * nickName : 测试
         * organization_ID : 0
         * organization_Name :
         * organization_No :
         * phone :
         * remark :
         * resultStatus :
         * returnBicycleDate :
         * rideCost : 1.5
         * startPos : 0
         * startTime :
         * tripDist : 0.01
         * tripTime : 84
         * userAddress :
         * userAge : 0
         * userId : 0
         * userImage : /userImage/201705241456082709989one.jpg
         * userSex : 0
         */

        private int bicycleId;
        private String bicycleNo;
        private double calorie;
        private String carRentalOrderDate;
        private String carRentalOrderId;
        private String carRentalOrderNo;
        private String carRentalOrderStatus;
        private String couponno;
        private String endTime;
        private String idCard;
        private String name;
        private String nickName;
        private String phone;
        private String resultStatus;
        private double rideCost;
        private int startPos;
        private double tripDist;
        private int tripTime;
        private int userId;
        private String userImage;


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

        public double getCalorie() {
            return calorie;
        }

        public void setCalorie(double calorie) {
            this.calorie = calorie;
        }

        public String getCarRentalOrderDate() {
            return carRentalOrderDate;
        }

        public void setCarRentalOrderDate(String carRentalOrderDate) {
            this.carRentalOrderDate = carRentalOrderDate;
        }

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

        public String getCarRentalOrderStatus() {
            return carRentalOrderStatus;
        }

        public void setCarRentalOrderStatus(String carRentalOrderStatus) {
            this.carRentalOrderStatus = carRentalOrderStatus;
        }

        public String getCouponno() {
            return couponno;
        }

        public void setCouponno(String couponno) {
            this.couponno = couponno;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public double getRideCost() {
            return rideCost;
        }

        public void setRideCost(double rideCost) {
            this.rideCost = rideCost;
        }

        public int getStartPos() {
            return startPos;
        }

        public void setStartPos(int startPos) {
            this.startPos = startPos;
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

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

    }
}
