package com.dcch.sharebike.moudle.user.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class UserInfo implements Serializable {


    /**
     * id : 24
     * name : 111
     * nickName : 中国
     * phone : 17701273631
     * idcard : null
     * useraddress : null
     * aggregateAmount : 3
     * pledgeCash : 199
     * minaggregate : 0
     * maxaggregate : 0
     * integral : 7
     * mileage : 0.07
     * password : null
     * usersex : 0
     * userage : 0
     * userimage : http://192.168.1.130:8080/MavenSSM/UserImage/201703231357486113345one.jpg
     * messagecode : null
     * cashStatus : 1
     * status : 1
     * resultStatus : 1
     */

    private int id;
    private String name;
    private String nickName;
    private String phone;
    private Object idcard;
    private Object useraddress;
    private int aggregateAmount;
    private int pledgeCash;
    private int minaggregate;
    private int maxaggregate;
    private int integral;//积分
    private double mileage;
    private Object password;
    private int usersex;
    private int userage;
    private String userimage;
    private Object messagecode;
    private int cashStatus;
    private int status;
    private String resultStatus;

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

    public Object getIdcard() {
        return idcard;
    }

    public void setIdcard(Object idcard) {
        this.idcard = idcard;
    }

    public Object getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(Object useraddress) {
        this.useraddress = useraddress;
    }

    public int getAggregateAmount() {
        return aggregateAmount;
    }

    public void setAggregateAmount(int aggregateAmount) {
        this.aggregateAmount = aggregateAmount;
    }

    public int getPledgeCash() {
        return pledgeCash;
    }

    public void setPledgeCash(int pledgeCash) {
        this.pledgeCash = pledgeCash;
    }

    public int getMinaggregate() {
        return minaggregate;
    }

    public void setMinaggregate(int minaggregate) {
        this.minaggregate = minaggregate;
    }

    public int getMaxaggregate() {
        return maxaggregate;
    }

    public void setMaxaggregate(int maxaggregate) {
        this.maxaggregate = maxaggregate;
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

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public int getUsersex() {
        return usersex;
    }

    public void setUsersex(int usersex) {
        this.usersex = usersex;
    }

    public int getUserage() {
        return userage;
    }

    public void setUserage(int userage) {
        this.userage = userage;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public Object getMessagecode() {
        return messagecode;
    }

    public void setMessagecode(Object messagecode) {
        this.messagecode = messagecode;
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
}
