package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.user.bean.CouponInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class CouponInfoAdapter extends CommonAdapter<CouponInfo.CouponsBean> {
    public CouponInfoAdapter(Context context, int layoutId, List<CouponInfo.CouponsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CouponInfo.CouponsBean couponsBean, int position) {
        holder.setText(R.id.couponNum, couponsBean.getCouponNo());
        holder.setText(R.id.expiry_date, couponsBean.getEndTime());
        holder.setText(R.id.coupon_amount, String.valueOf(couponsBean.getAmount()) + "元");
    }
}
