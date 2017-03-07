package com.dcch.sharebike.alipay;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class AliPay {
    private Activity activity;
    public AliPay(Activity activity) {
        this.activity = activity;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

}
