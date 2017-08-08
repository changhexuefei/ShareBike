package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.user.bean.RedPacketInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class RedPacketInfoAdapter extends CommonAdapter<RedPacketInfo.MerchanBillBean> {

    public RedPacketInfoAdapter(Context context, int layoutId, List<RedPacketInfo.MerchanBillBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RedPacketInfo.MerchanBillBean merchanBillBean, int position) {
        holder.setText(R.id.receiveNum, merchanBillBean.getUseramount() + "元");
        holder.setText(R.id.receiveTime, merchanBillBean.getBilltime());

    }
}
