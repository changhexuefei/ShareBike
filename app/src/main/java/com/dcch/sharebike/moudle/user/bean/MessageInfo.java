package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class MessageInfo {
    /**
     * resultStatus : 1
     * activitys : [{"activityimage":"http://www.70bikes.com/MavenSSM/ActivityImage/7toad.png","activityname":"赠送代金券","activityno":"coupon001","activitystatus":0,"activityurl":"http://www.70bikes.com/70bikes/activity/rechargeAgreement.jsp","endtime":"2017-04-13","starttime":"2017-04-03"}]
     */

    private String resultStatus;
    private List<ActivitysBean> activitys;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<ActivitysBean> getActivitys() {
        return activitys;
    }

    public void setActivitys(List<ActivitysBean> activitys) {
        this.activitys = activitys;
    }

    public static class ActivitysBean {
        /**
         * activityimage : http://www.70bikes.com/MavenSSM/ActivityImage/7toad.png
         * activityname : 赠送代金券
         * activityno : coupon001
         * activitystatus : 0
         * activityurl : http://www.70bikes.com/70bikes/activity/rechargeAgreement.jsp
         * endtime : 2017-04-13
         * starttime : 2017-04-03
         */

        private String activityimage;
        private String activityname;
        private String activityno;
        private int activitystatus;
        private String activityurl;
        private String endtime;
        private String starttime;

        public String getActivityimage() {
            return activityimage;
        }

        public void setActivityimage(String activityimage) {
            this.activityimage = activityimage;
        }

        public String getActivityname() {
            return activityname;
        }

        public void setActivityname(String activityname) {
            this.activityname = activityname;
        }

        public String getActivityno() {
            return activityno;
        }

        public void setActivityno(String activityno) {
            this.activityno = activityno;
        }

        public int getActivitystatus() {
            return activitystatus;
        }

        public void setActivitystatus(int activitystatus) {
            this.activitystatus = activitystatus;
        }

        public String getActivityurl() {
            return activityurl;
        }

        public void setActivityurl(String activityurl) {
            this.activityurl = activityurl;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }
    }
}
