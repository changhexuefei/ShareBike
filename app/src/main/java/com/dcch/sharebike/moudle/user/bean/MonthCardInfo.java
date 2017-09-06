package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class MonthCardInfo {
    /**
     * resultStatus : 1
     * cardHolder : [{"cardType":"一个月","cardprice":2,"createTime":"2017-08-23 10:12:47","delflag":1,"discount":1,"discountbefore":20,"id":1},{"cardType":"三个月","cardprice":5,"createTime":"2017-08-25 15:28:14","delflag":1,"discount":0.8,"discountbefore":40,"id":2},{"cardType":"十二个月","cardprice":20,"createTime":"2017-08-25 15:28:14","delflag":1,"discount":0.5,"discountbefore":100,"id":3}]
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
         * discount : 1
         * discountbefore : 20
         * id : 1
         */

        private String cardType;
        private float cardprice;
        private String createTime;
        private int delflag;
        private double discount;
        private int discountbefore;
        private int id;

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public float getCardprice() {
            return cardprice;
        }

        public void setCardprice(float cardprice) {
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

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getDiscountbefore() {
            return discountbefore;
        }

        public void setDiscountbefore(int discountbefore) {
            this.discountbefore = discountbefore;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


//    /**
//     * resultStatus : 1
//     * cardHolder : [{"cardType":"一个月","cardprice":2,"createTime":"2017-08-23 10:12:47","delflag":1,"id":1},{"cardType":"三个月","cardprice":5,"createTime":"2017-08-25 15:28:14","delflag":1,"id":2},{"cardType":"十二个月","cardprice":20,"createTime":"2017-08-25 15:28:14","delflag":1,"id":3}]
//     */
//
//    private String resultStatus;
//    private List<CardHolderBean> cardHolder;
//
//    public String getResultStatus() {
//        return resultStatus;
//    }
//
//    public void setResultStatus(String resultStatus) {
//        this.resultStatus = resultStatus;
//    }
//
//    public List<CardHolderBean> getCardHolder() {
//        return cardHolder;
//    }
//
//    public void setCardHolder(List<CardHolderBean> cardHolder) {
//        this.cardHolder = cardHolder;
//    }
//
//    public static class CardHolderBean {
//        /**
//         * cardType : 一个月
//         * cardprice : 2
//         * createTime : 2017-08-23 10:12:47
//         * delflag : 1
//         * id : 1
//         */
//
//        private String cardType;
//        private double cardprice;
//        private String createTime;
//        private int delflag;
//        private int id;
//
//        public String getCardType() {
//            return cardType;
//        }
//
//        public void setCardType(String cardType) {
//            this.cardType = cardType;
//        }
//
//        public double getCardprice() {
//            return cardprice;
//        }
//
//        public void setCardprice(double cardprice) {
//            this.cardprice = cardprice;
//        }
//
//        public String getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//            this.createTime = createTime;
//        }
//
//        public int getDelflag() {
//            return delflag;
//        }
//
//        public void setDelflag(int delflag) {
//            this.delflag = delflag;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//    }
}
