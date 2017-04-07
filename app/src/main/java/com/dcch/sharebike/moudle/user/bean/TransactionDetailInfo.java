package com.dcch.sharebike.moudle.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class TransactionDetailInfo {

    /**
     * resultStatus : 1
     * payBills : [{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406123919","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406123751","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406123320","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406123201","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406113657","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406112735","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406111845","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406110935","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406091533","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170406090915","total_price":0.01,"trade_type":"","transaction_id":"","userId":""},{"appid":"","billId":0,"mch_id":"","nonce_str":"","openid":"","out_trade_no":"","paymode":"微信支付","status":0,"time_end":"20170405133228","total_price":0.01,"trade_type":"","transaction_id":"","userId":""}]
     */

    private String resultStatus;
    private List<PayBillsBean> payBills;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public List<PayBillsBean> getPayBills() {
        return payBills;
    }

    public void setPayBills(List<PayBillsBean> payBills) {
        this.payBills = payBills;
    }

    public static class PayBillsBean {
        /**
         * appid :
         * billId : 0
         * mch_id :
         * nonce_str :
         * openid :
         * out_trade_no :
         * paymode : 微信支付
         * status : 0
         * time_end : 20170406123919
         * total_price : 0.01
         * trade_type :
         * transaction_id :
         * userId :
         */

        private String paymode;
        private int status;
        private String time_end;
        private double total_price;

        public String getPaymode() {
            return paymode;
        }

        public void setPaymode(String paymode) {
            this.paymode = paymode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }
    }
}
