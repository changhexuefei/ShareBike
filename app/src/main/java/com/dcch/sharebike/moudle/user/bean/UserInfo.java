package com.dcch.sharebike.moudle.user.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class UserInfo implements Serializable {


    /**
     * id : 119
     * name :
     * nickName : 17701273631
     * phone : 17701273631
     * idcard : null
     * useraddress : null
     * aggregateAmount : 0
     * pledgeCash : 0
     * minaggregate : 0
     * maxaggregate : 0
     * integral : 0
     * mileage : null
     * password : null
     * usersex : 0
     * userage : 0
     * userimage : null
     * messagecode : 1
     * cashStatus : 0
     * status : 0
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
    private int integral;
    private Object mileage;
    private Object password;
    private int usersex;
    private int userage;
    private Object userimage;
    private String messagecode;
    private int cashStatus;
    private int status;

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

    public Object getMileage() {
        return mileage;
    }

    public void setMileage(Object mileage) {
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

    public Object getUserimage() {
        return userimage;
    }

    public void setUserimage(Object userimage) {
        this.userimage = userimage;
    }

    public String getMessagecode() {
        return messagecode;
    }

    public void setMessagecode(String messagecode) {
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
}
