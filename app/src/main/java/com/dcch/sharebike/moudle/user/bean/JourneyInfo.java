package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class JourneyInfo {


    /**
     * resultStatus : 1
     * carrOrders : [{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-24 20:03:54","carRentalOrderId":"6bbe2cb927cf45e2bd36c0646f5497b5","carRentalOrderNo":"carrental201703242003548255288","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1,"startPos":0,"startTime":"","tripDist":0.01,"tripTime":1,"userAddress":"","userAge":0,"userId":0,"userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-24 19:59:42","carRentalOrderId":"2d178256607644499976e2f86e6b1171","carRentalOrderNo":"carrental201703241959429671337","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":142,"startPos":0,"startTime":"","tripDist":0,"tripTime":8461,"userAddress":"","userAge":0,"userId":0,"userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-31 18:09:23","carRentalOrderId":"3a3f12689f74428fb714a0064b0ae149","carRentalOrderNo":"carrental201703311809239357158","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1,"startPos":0,"startTime":"","tripDist":0,"tripTime":0,"userAddress":"","userAge":0,"userId":0,"userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-31 17:57:43","carRentalOrderId":"3a42e7b4d9c14e83947dce2173b8354d","carRentalOrderNo":"carrental201703311757435465640","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1,"startPos":0,"startTime":"","tripDist":0,"tripTime":0,"userAddress":"","userAge":0,"userId":0,"userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-31 18:24:42","carRentalOrderId":"49c2d7484ba1402bbc73fb2c6cd5bca4","carRentalOrderNo":"carrental201703311824425412227","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1,"startPos":0,"startTime":"","tripDist":0,"tripTime":0,"userAddress":"","userAge":0,"userId":0,"userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-31 17:13:46","carRentalOrderId":"50e972a45c2a4e82aebce1343cd2dba6","carRentalOrderNo":"carrental201703311713466102005","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1,"startPos":0,"startTime":"","tripDist":0,"tripTime":0,"userAddress":"","userAge":0,"userId":0,"userSex":0},{"aggregateAmount":0,"bicycleId":0,"bicycleNo":"1101000001","calorie":0,"carRentalOrderDate":"2017-03-31 18:26:40","carRentalOrderId":"5fead6aad40c4b21ab945aeb92a748c7","carRentalOrderNo":"carrental201703311826405714370","carRentalOrderStatus":"","endTime":"","idCard":"","integral":0,"name":"111","nickName":"","organization_ID":0,"organization_Name":"","organization_No":"","phone":"","remark":"","resultStatus":"","returnBicycleDate":"","rideCost":1,"startPos":0,"startTime":"","tripDist":0,"tripTime":0,"userAddress":"","userAge":0,"userId":0,"userSex":0}]
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
         * bicycleNo : 1101000001
         * calorie : 0
         * carRentalOrderDate : 2017-03-24 20:03:54
         * carRentalOrderId : 6bbe2cb927cf45e2bd36c0646f5497b5
         * carRentalOrderNo : carrental201703242003548255288
         * carRentalOrderStatus :
         * endTime :
         * idCard :
         * integral : 0
         * name : 111
         * nickName :
         * organization_ID : 0
         * organization_Name :
         * organization_No :
         * phone :
         * remark :
         * resultStatus :
         * returnBicycleDate :
         * rideCost : 1
         * startPos : 0
         * startTime :
         * tripDist : 0.01
         * tripTime : 1
         * userAddress :
         * userAge : 0
         * userId : 0
         * userSex : 0
         */

        private int aggregateAmount;
        private int bicycleId;
        private String bicycleNo;
        private int calorie;
        private String carRentalOrderDate;
        private String carRentalOrderId;
        private String carRentalOrderNo;
        private String carRentalOrderStatus;
        private String endTime;
        private String idCard;
        private int integral;
        private String name;
        private String nickName;
        private int organization_ID;
        private String organization_Name;
        private String organization_No;
        private String phone;
        private String remark;
        private String resultStatus;
        private String returnBicycleDate;
        private float rideCost;
        private int startPos;
        private String startTime;
        private double tripDist;
        private int tripTime;
        private String userAddress;
        private int userAge;
        private int userId;
        private int userSex;

        public int getAggregateAmount() {
            return aggregateAmount;
        }

        public void setAggregateAmount(int aggregateAmount) {
            this.aggregateAmount = aggregateAmount;
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

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
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

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
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

        public int getOrganization_ID() {
            return organization_ID;
        }

        public void setOrganization_ID(int organization_ID) {
            this.organization_ID = organization_ID;
        }

        public String getOrganization_Name() {
            return organization_Name;
        }

        public void setOrganization_Name(String organization_Name) {
            this.organization_Name = organization_Name;
        }

        public String getOrganization_No() {
            return organization_No;
        }

        public void setOrganization_No(String organization_No) {
            this.organization_No = organization_No;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public String getReturnBicycleDate() {
            return returnBicycleDate;
        }

        public void setReturnBicycleDate(String returnBicycleDate) {
            this.returnBicycleDate = returnBicycleDate;
        }

        public float getRideCost() {
            return rideCost;
        }

        public void setRideCost(float rideCost) {
            this.rideCost = rideCost;
        }

        public int getStartPos() {
            return startPos;
        }

        public void setStartPos(int startPos) {
            this.startPos = startPos;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
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

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public int getUserAge() {
            return userAge;
        }

        public void setUserAge(int userAge) {
            this.userAge = userAge;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserSex() {
            return userSex;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }
    }
}
