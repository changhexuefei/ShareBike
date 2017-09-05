package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public class RideResultInfo {
    /**
     * resultStatus : 1
     * resultGift : 0
     * merchantid :
     * merchantinfo :
     * merchantimageurl :
     * carRentalInfo : {"amount":0,"bicycleNo":"","calorie":0,"carRentalOrderId":"b48a1030f2424ecb9dbec0e4be32e4c6","cardFlag":1,"couponAmount":0,"couponno":"nocoupon","finalCast":0.5,"orderCast":0,"resultStatus":"","rideCost":0.5,"ridetimes":0,"status":0,"tripDist":0,"tripTime":2}
     */

    private String resultStatus;
    private String resultGift;
    private String merchantid;
    private String merchantinfo;
    private String merchantimageurl;
    private CarRentalInfoBean carRentalInfo;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultGift() {
        return resultGift;
    }

    public void setResultGift(String resultGift) {
        this.resultGift = resultGift;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getMerchantinfo() {
        return merchantinfo;
    }

    public void setMerchantinfo(String merchantinfo) {
        this.merchantinfo = merchantinfo;
    }

    public String getMerchantimageurl() {
        return merchantimageurl;
    }

    public void setMerchantimageurl(String merchantimageurl) {
        this.merchantimageurl = merchantimageurl;
    }

    public CarRentalInfoBean getCarRentalInfo() {
        return carRentalInfo;
    }

    public void setCarRentalInfo(CarRentalInfoBean carRentalInfo) {
        this.carRentalInfo = carRentalInfo;
    }

    public static class CarRentalInfoBean {
        /**
         * amount : 0
         * bicycleNo :
         * calorie : 0
         * carRentalOrderId : b48a1030f2424ecb9dbec0e4be32e4c6
         * cardFlag : 1
         * couponAmount : 0
         * couponno : nocoupon
         * finalCast : 0.5
         * orderCast : 0
         * resultStatus :
         * rideCost : 0.5
         * ridetimes : 0
         * status : 0
         * tripDist : 0
         * tripTime : 2
         */

        private double amount;
        private String bicycleNo;
        private double calorie;
        private String carRentalOrderId;
        private int cardFlag;
        private double couponAmount;
        private String couponno;
        private double finalCast;
        private double orderCast;
        private String resultStatus;
        private double rideCost;
        private int ridetimes;
        private int status;
        private double tripDist;
        private int tripTime;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
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

        public String getCarRentalOrderId() {
            return carRentalOrderId;
        }

        public void setCarRentalOrderId(String carRentalOrderId) {
            this.carRentalOrderId = carRentalOrderId;
        }

        public int getCardFlag() {
            return cardFlag;
        }

        public void setCardFlag(int cardFlag) {
            this.cardFlag = cardFlag;
        }

        public double getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(double couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getCouponno() {
            return couponno;
        }

        public void setCouponno(String couponno) {
            this.couponno = couponno;
        }

        public double getFinalCast() {
            return finalCast;
        }

        public void setFinalCast(double finalCast) {
            this.finalCast = finalCast;
        }

        public double getOrderCast() {
            return orderCast;
        }

        public void setOrderCast(double orderCast) {
            this.orderCast = orderCast;
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

        public int getRidetimes() {
            return ridetimes;
        }

        public void setRidetimes(int ridetimes) {
            this.ridetimes = ridetimes;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
    }


//    /**
//     * resultStatus : 1
//     * resultGift : 1
//     * merchantid : 3
//     * merchantinfo : 信掌柜
//     * merchantimageurl : http://www.70bikes.com/bikeImage/advertisement/xinzhanggui_icon.png
//     * carRentalInfo : {"amount":0,"bicycleNo":"","calorie":0,"carRentalOrderId":"bc283ce6fe9443ae9aff004acdd05419","cardFlag":1,"couponAmount":0,"couponno":"nocoupon","finalCast":0,"orderCast":0,"resultStatus":"","rideCost":0,"status":0,"tripDist":0,"tripTime":16}
//     */
//
//    private String resultStatus;
//    private String resultGift;
//    private String merchantid;
//    private String merchantinfo;
//    private String merchantimageurl;
//    private CarRentalInfoBean carRentalInfo;
//
//    public String getResultStatus() {
//        return resultStatus;
//    }
//
//    public void setResultStatus(String resultStatus) {
//        this.resultStatus = resultStatus;
//    }
//
//    public String getResultGift() {
//        return resultGift;
//    }
//
//    public void setResultGift(String resultGift) {
//        this.resultGift = resultGift;
//    }
//
//    public String getMerchantid() {
//        return merchantid;
//    }
//
//    public void setMerchantid(String merchantid) {
//        this.merchantid = merchantid;
//    }
//
//    public String getMerchantinfo() {
//        return merchantinfo;
//    }
//
//    public void setMerchantinfo(String merchantinfo) {
//        this.merchantinfo = merchantinfo;
//    }
//
//    public String getMerchantimageurl() {
//        return merchantimageurl;
//    }
//
//    public void setMerchantimageurl(String merchantimageurl) {
//        this.merchantimageurl = merchantimageurl;
//    }
//
//    public CarRentalInfoBean getCarRentalInfo() {
//        return carRentalInfo;
//    }
//
//    public void setCarRentalInfo(CarRentalInfoBean carRentalInfo) {
//        this.carRentalInfo = carRentalInfo;
//    }
//
//    public static class CarRentalInfoBean {
//        /**
//         * amount : 0
//         * bicycleNo :
//         * calorie : 0
//         * carRentalOrderId : bc283ce6fe9443ae9aff004acdd05419
//         * cardFlag : 1
//         * couponAmount : 0
//         * couponno : nocoupon
//         * finalCast : 0
//         * orderCast : 0
//         * resultStatus :
//         * rideCost : 0
//         * status : 0
//         * tripDist : 0
//         * tripTime : 16
//         */
//
//        private double amount;
//        private String bicycleNo;
//        private double calorie;
//        private String carRentalOrderId;
//        private int cardFlag;
//        private int couponAmount;
//        private String couponno;
//        private double finalCast;
//        private double orderCast;
//        private String resultStatus;
//        private double rideCost;
//        private int status;
//        private double tripDist;
//        private int tripTime;
//
//        public double getAmount() {
//            return amount;
//        }
//
//        public void setAmount(double amount) {
//            this.amount = amount;
//        }
//
//        public String getBicycleNo() {
//            return bicycleNo;
//        }
//
//        public void setBicycleNo(String bicycleNo) {
//            this.bicycleNo = bicycleNo;
//        }
//
//        public double getCalorie() {
//            return calorie;
//        }
//
//        public void setCalorie(double calorie) {
//            this.calorie = calorie;
//        }
//
//        public String getCarRentalOrderId() {
//            return carRentalOrderId;
//        }
//
//        public void setCarRentalOrderId(String carRentalOrderId) {
//            this.carRentalOrderId = carRentalOrderId;
//        }
//
//        public int getCardFlag() {
//            return cardFlag;
//        }
//
//        public void setCardFlag(int cardFlag) {
//            this.cardFlag = cardFlag;
//        }
//
//        public int getCouponAmount() {
//            return couponAmount;
//        }
//
//        public void setCouponAmount(int couponAmount) {
//            this.couponAmount = couponAmount;
//        }
//
//        public String getCouponno() {
//            return couponno;
//        }
//
//        public void setCouponno(String couponno) {
//            this.couponno = couponno;
//        }
//
//        public double getFinalCast() {
//            return finalCast;
//        }
//
//        public void setFinalCast(double finalCast) {
//            this.finalCast = finalCast;
//        }
//
//        public double getOrderCast() {
//            return orderCast;
//        }
//
//        public void setOrderCast(double orderCast) {
//            this.orderCast = orderCast;
//        }
//
//        public String getResultStatus() {
//            return resultStatus;
//        }
//
//        public void setResultStatus(String resultStatus) {
//            this.resultStatus = resultStatus;
//        }
//
//        public double getRideCost() {
//            return rideCost;
//        }
//
//        public void setRideCost(double rideCost) {
//            this.rideCost = rideCost;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//
//        public double getTripDist() {
//            return tripDist;
//        }
//
//        public void setTripDist(double tripDist) {
//            this.tripDist = tripDist;
//        }
//
//        public int getTripTime() {
//            return tripTime;
//        }
//
//        public void setTripTime(int tripTime) {
//            this.tripTime = tripTime;
//        }
//    }


//    /**
//     * resultStatus : 1
//     * carRentalInfo : {"amount":0,"bicycleNo":"","calorie":0,"carRentalOrderId":"c109794e26e640c7bf1c37cc75e99a0b","couponAmount":0,"couponno":"nocoupon","finalCast":0.5,"orderCast":0,"resultStatus":"","rideCost":0.5,"status":0,"tripDist":0,"tripTime":1}
//     */
//
//    private String resultStatus;
//    private CarRentalInfoBean carRentalInfo;
//
//    public String getResultStatus() {
//        return resultStatus;
//    }
//
//    public void setResultStatus(String resultStatus) {
//        this.resultStatus = resultStatus;
//    }
//
//    public CarRentalInfoBean getCarRentalInfo() {
//        return carRentalInfo;
//    }
//
//    public void setCarRentalInfo(CarRentalInfoBean carRentalInfo) {
//        this.carRentalInfo = carRentalInfo;
//    }
//
//    public static class CarRentalInfoBean {
//        /**
//         * amount : 0
//         * bicycleNo :
//         * calorie : 0
//         * carRentalOrderId : c109794e26e640c7bf1c37cc75e99a0b
//         * couponAmount : 0
//         * couponno : nocoupon
//         * finalCast : 0.5
//         * orderCast : 0
//         * resultStatus :
//         * rideCost : 0.5
//         * status : 0
//         * tripDist : 0
//         * tripTime : 1
//         */
//
//        private double amount;
//        private String bicycleNo;
//        private double calorie;
//        private String carRentalOrderId;
//        private float couponAmount;
//        private String couponno;
//        private double finalCast;
//        private double orderCast;
//        private double rideCost;
//        private int status;
//        private double tripDist;
//        private int tripTime;
//
//        public double getAmount() {
//            return amount;
//        }
//
//        public void setAmount(double amount) {
//            this.amount = amount;
//        }
//
//        public String getBicycleNo() {
//            return bicycleNo;
//        }
//
//        public void setBicycleNo(String bicycleNo) {
//            this.bicycleNo = bicycleNo;
//        }
//
//        public double getCalorie() {
//            return calorie;
//        }
//
//        public void setCalorie(double calorie) {
//            this.calorie = calorie;
//        }
//
//        public String getCarRentalOrderId() {
//            return carRentalOrderId;
//        }
//
//        public void setCarRentalOrderId(String carRentalOrderId) {
//            this.carRentalOrderId = carRentalOrderId;
//        }
//
//        public float getCouponAmount() {
//            return couponAmount;
//        }
//
//        public void setCouponAmount(float couponAmount) {
//            this.couponAmount = couponAmount;
//        }
//
//        public String getCouponno() {
//            return couponno;
//        }
//
//        public void setCouponno(String couponno) {
//            this.couponno = couponno;
//        }
//
//        public double getFinalCast() {
//            return finalCast;
//        }
//
//        public void setFinalCast(double finalCast) {
//            this.finalCast = finalCast;
//        }
//
//        public double getOrderCast() {
//            return orderCast;
//        }
//
//        public void setOrderCast(double orderCast) {
//            this.orderCast = orderCast;
//        }
//
//        public double getRideCost() {
//            return rideCost;
//        }
//
//        public void setRideCost(double rideCost) {
//            this.rideCost = rideCost;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//
//        public double getTripDist() {
//            return tripDist;
//        }
//
//        public void setTripDist(double tripDist) {
//            this.tripDist = tripDist;
//        }
//
//        public int getTripTime() {
//            return tripTime;
//        }
//
//        public void setTripTime(int tripTime) {
//            this.tripTime = tripTime;
//        }
//    }
}
