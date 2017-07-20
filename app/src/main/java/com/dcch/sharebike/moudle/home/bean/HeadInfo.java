package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class HeadInfo {


    /**
     * resultStatus : 1
     * headAdvertisement : {"activityUrl":"","delflag":0,"headactivityUrl":"http://www.70bikes.com/","headimageUrl":"http://www.70bikes.com/bikeImage/headadvertisement/headktv.jpg","imageUrl":"","merchantid":"1","remark":"test","title":""}
     */

    private String resultStatus;
    private HeadAdvertisementBean headAdvertisement;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public HeadAdvertisementBean getHeadAdvertisement() {
        return headAdvertisement;
    }

    public void setHeadAdvertisement(HeadAdvertisementBean headAdvertisement) {
        this.headAdvertisement = headAdvertisement;
    }

    public static class HeadAdvertisementBean {
        /**
         * activityUrl :
         * delflag : 0
         * headactivityUrl : http://www.70bikes.com/
         * headimageUrl : http://www.70bikes.com/bikeImage/headadvertisement/headktv.jpg
         * imageUrl :
         * merchantid : 1
         * remark : test
         * title :
         */

        private String activityUrl;
        private int delflag;
        private String headactivityUrl;
        private String headimageUrl;
        private String imageUrl;
        private String merchantid;
        private String remark;
        private String title;

        public String getActivityUrl() {
            return activityUrl;
        }

        public void setActivityUrl(String activityUrl) {
            this.activityUrl = activityUrl;
        }

        public int getDelflag() {
            return delflag;
        }

        public void setDelflag(int delflag) {
            this.delflag = delflag;
        }

        public String getHeadactivityUrl() {
            return headactivityUrl;
        }

        public void setHeadactivityUrl(String headactivityUrl) {
            this.headactivityUrl = headactivityUrl;
        }

        public String getHeadimageUrl() {
            return headimageUrl;
        }

        public void setHeadimageUrl(String headimageUrl) {
            this.headimageUrl = headimageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getMerchantid() {
            return merchantid;
        }

        public void setMerchantid(String merchantid) {
            this.merchantid = merchantid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
