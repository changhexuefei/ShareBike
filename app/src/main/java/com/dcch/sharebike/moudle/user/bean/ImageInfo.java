package com.dcch.sharebike.moudle.user.bean;

/**
 * Created by gao on 2017/7/17.
 */

public class ImageInfo {


    /**
     * resultStatus : 1
     * bicycleImage : {"delflag":0,"imageInfo":"小黄人","imageUrl":"http://114.112.86.38/bikeImage/first.png","remark":""}
     */

    private String resultStatus;
    private BicycleImageBean bicycleImage;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public BicycleImageBean getBicycleImage() {
        return bicycleImage;
    }

    public void setBicycleImage(BicycleImageBean bicycleImage) {
        this.bicycleImage = bicycleImage;
    }

    public static class BicycleImageBean {
        /**
         * delflag : 0
         * imageInfo : 小黄人
         * imageUrl : http://114.112.86.38/bikeImage/first.png
         * remark :
         */

        private int delflag;
        private String imageInfo;
        private String imageUrl;
        private String remark;

        public int getDelflag() {
            return delflag;
        }

        public void setDelflag(int delflag) {
            this.delflag = delflag;
        }

        public String getImageInfo() {
            return imageInfo;
        }

        public void setImageInfo(String imageInfo) {
            this.imageInfo = imageInfo;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
