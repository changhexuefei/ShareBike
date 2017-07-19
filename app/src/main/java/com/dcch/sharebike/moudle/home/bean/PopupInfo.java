package com.dcch.sharebike.moudle.home.bean;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class PopupInfo {

    /**
     * resultStatus : 1
     * advertisement : {"activityUrl":"","delflag":0,"headactivityUrl":"","headimageUrl":"","imageUrl":"http://www.70bikes.com/bikeImage/two.png","merchantid":"","remark":"test"}
     */

    private String resultStatus;
    private AdvertisementBean advertisement;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public AdvertisementBean getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(AdvertisementBean advertisement) {
        this.advertisement = advertisement;
    }

    public static class AdvertisementBean {
        /**
         * activityUrl :
         * delflag : 0
         * headactivityUrl :
         * headimageUrl :
         * imageUrl : http://www.70bikes.com/bikeImage/two.png
         * merchantid :
         * remark : test
         */

        private String activityUrl;
        private int delflag;
        private String headactivityUrl;
        private String headimageUrl;
        private String imageUrl;
        private String merchantid;
        private String remark;

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
    }
}
