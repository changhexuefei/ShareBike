package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class CouponInfo {

    /**
     * resultStatus : 1
     * coupons : [{"amount":1,"couponId":10,"couponNo":"LgeX9n20170427161702","delFlag":0,"endTime":"2017-05-02 16:17:02","startTime":"2017-04-27 16:17:02","userId":172}]
     */

    private String resultStatus;
    private List<CouponsBean> coupons;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<CouponsBean> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponsBean> coupons) {
        this.coupons = coupons;
    }

    public static class CouponsBean {
        /**
         * amount : 1
         * couponId : 10
         * couponNo : LgeX9n20170427161702
         * delFlag : 0
         * endTime : 2017-05-02 16:17:02
         * startTime : 2017-04-27 16:17:02
         * userId : 172
         */

        private int amount;
        private int couponId;
        private String couponNo;
        private int delFlag;
        private String endTime;
        private String startTime;
        private int userId;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public String getCouponNo() {
            return couponNo;
        }

        public void setCouponNo(String couponNo) {
            this.couponNo = couponNo;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
