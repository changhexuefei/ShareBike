package com.dcch.sharebike.moudle.user.bean;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class WeixinReturnInfo {

    private String appid;
    private String mch_id;
    private Object device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private Object err_code;
    private Object err_code_des;
    private String trade_type;
    private String prepay_id;
    private Object code_url;
    private String resultStatus;
    private String return_code;
    private String return_msg;
    private String timestamp;
    private String packageid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public Object getDevice_info() {
        return device_info;
    }

    public void setDevice_info(Object device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public Object getErr_code() {
        return err_code;
    }

    public void setErr_code(Object err_code) {
        this.err_code = err_code;
    }

    public Object getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(Object err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public Object getCode_url() {
        return code_url;
    }

    public void setCode_url(Object code_url) {
        this.code_url = code_url;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }
}
