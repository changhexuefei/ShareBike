package com.dcch.sharebike.moudle.user.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class UserInfo implements Serializable {
    /**
     * id : 214
     * name : 郭焕新
     * nickName : 测试
     * phone : 17701273631
     * idcard : 13043419910216052X
     * useraddress : null
     * aggregateAmount : 999.3
     * merchanBillAmount : 43
     * pledgeCash : 98
     * minaggregate : 0
     * maxaggregate : 0
     * integral : 565
     * mileage : 28.01
     * password : null
     * usersex : 0
     * userage : 0
     * userimage : http://www.70bikes.com/userImage/201708141433324051496one.jpg
     * calorie : 867.89355
     * messagecode : null
     * cashStatus : 1
     * status : 1
     * resultStatus : 1
     * token : Vatt2m8VVshKm60Cd120170830142125
     * registerDate : 2017-06-27 13:56:54
     * remark : null
     * count : 0
     * cardAmount : 0
     * ridetimes : 0
     * rideDay : 0
     * starttime : null
     * endtime : null
     * delflag : 0
     */

    private int id;
    private String name;
    private String nickName;
    private String phone;
    private String idcard;
    private double aggregateAmount;
    private double merchanBillAmount;
    private int pledgeCash;
    private int integral;
    private double mileage;
    private String userimage;
    private double calorie;
    private int cashStatus;
    private int status;
    private String resultStatus;
    private String token;
    private int rideDay;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public double getAggregateAmount() {
        return aggregateAmount;
    }

    public void setAggregateAmount(double aggregateAmount) {
        this.aggregateAmount = aggregateAmount;
    }

    public double getMerchanBillAmount() {
        return merchanBillAmount;
    }

    public void setMerchanBillAmount(double merchanBillAmount) {
        this.merchanBillAmount = merchanBillAmount;
    }

    public int getPledgeCash() {
        return pledgeCash;
    }

    public void setPledgeCash(int pledgeCash) {
        this.pledgeCash = pledgeCash;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }


    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public int getCashStatus() {
        return cashStatus;
    }

    public void setCashStatus(int cashStatus) {
        this.cashStatus = cashStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRideDay() {
        return rideDay;
    }

    public void setRideDay(int rideDay) {
        this.rideDay = rideDay;
    }

//

//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getNickName() {
//        return nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getIdcard() {
//        return idcard;
//    }
//
//    public void setIdcard(String idcard) {
//        this.idcard = idcard;
//    }
//
//    public double getAggregateAmount() {
//        return aggregateAmount;
//    }
//
//    public void setAggregateAmount(double aggregateAmount) {
//        this.aggregateAmount = aggregateAmount;
//    }
//
//    public double getMerchanBillAmount() {
//        return merchanBillAmount;
//    }
//
//    public void setMerchanBillAmount(double merchanBillAmount) {
//        this.merchanBillAmount = merchanBillAmount;
//    }
//
//    public int getPledgeCash() {
//        return pledgeCash;
//    }
//
//    public void setPledgeCash(int pledgeCash) {
//        this.pledgeCash = pledgeCash;
//    }
//
//    public int getIntegral() {
//        return integral;
//    }
//
//    public void setIntegral(int integral) {
//        this.integral = integral;
//    }
//
//    public double getMileage() {
//        return mileage;
//    }
//
//    public void setMileage(double mileage) {
//        this.mileage = mileage;
//    }
//
//    public String getUserimage() {
//        return userimage;
//    }
//
//    public void setUserimage(String userimage) {
//        this.userimage = userimage;
//    }
//
//    public double getCalorie() {
//        return calorie;
//    }
//
//    public void setCalorie(double calorie) {
//        this.calorie = calorie;
//    }
//
//    public int getCashStatus() {
//        return cashStatus;
//    }
//
//    public void setCashStatus(int cashStatus) {
//        this.cashStatus = cashStatus;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public String getResultStatus() {
//        return resultStatus;
//    }
//
//    public void setResultStatus(String resultStatus) {
//        this.resultStatus = resultStatus;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }


}
