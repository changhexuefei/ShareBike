package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class MonthCardInfo {


    /**
     * resultStatus : 1
     * cardHolder : [{"cardType":"一个月","cardprice":2,"createTime":"2017-08-23 10:12:47","delflag":1,"id":1},{"cardType":"三个月","cardprice":5,"createTime":"2017-08-25 15:28:14","delflag":1,"id":2},{"cardType":"十二个月","cardprice":20,"createTime":"2017-08-25 15:28:14","delflag":1,"id":3}]
     */

    private String resultStatus;
    private List<CardHolderBean> cardHolder;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<CardHolderBean> getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(List<CardHolderBean> cardHolder) {
        this.cardHolder = cardHolder;
    }

    public static class CardHolderBean {
        /**
         * cardType : 一个月
         * cardprice : 2
         * createTime : 2017-08-23 10:12:47
         * delflag : 1
         * id : 1
         */

        private String cardType;
        private int cardprice;
        private String createTime;
        private int delflag;
        private int id;

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public int getCardprice() {
            return cardprice;
        }

        public void setCardprice(int cardprice) {
            this.cardprice = cardprice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDelflag() {
            return delflag;
        }

        public void setDelflag(int delflag) {
            this.delflag = delflag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
