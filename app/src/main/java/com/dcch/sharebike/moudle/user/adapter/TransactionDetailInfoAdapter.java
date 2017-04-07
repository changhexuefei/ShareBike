package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.user.bean.TransactionDetailInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class TransactionDetailInfoAdapter extends CommonAdapter<TransactionDetailInfo.PayBillsBean> {
    public TransactionDetailInfoAdapter(Context context, int layoutId, List<TransactionDetailInfo.PayBillsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TransactionDetailInfo.PayBillsBean payBillsBean, int position) {
//            holder.setText(R.id.dealResult,payBillsBean.)
        holder.setText(R.id.dealTime,payBillsBean.getTime_end());
        holder.setText(R.id.dealNum, String.valueOf(payBillsBean.getTotal_price()));
        holder.setText(R.id.dealType,payBillsBean.getPaymode());
    }

}
