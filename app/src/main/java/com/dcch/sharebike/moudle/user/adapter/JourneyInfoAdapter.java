package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.user.bean.JourneyInfo;
import com.dcch.sharebike.utils.MapUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class JourneyInfoAdapter extends CommonAdapter<JourneyInfo.CarrOrdersBean> {
    public JourneyInfoAdapter(Context context, int layoutId, List<JourneyInfo.CarrOrdersBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, JourneyInfo.CarrOrdersBean carrOrdersBean, int position) {
        holder.setText(R.id.journeyTime,carrOrdersBean.getCarRentalOrderDate());
        holder.setText(R.id.bikeNum, carrOrdersBean.getBicycleNo());
        holder.setText(R.id.duration, String.valueOf(MapUtil.timeFormatter(carrOrdersBean.getTripTime())));
        holder.setText(R.id.money_amount, String.valueOf(carrOrdersBean.getRideCost())+"元");
    }
}
