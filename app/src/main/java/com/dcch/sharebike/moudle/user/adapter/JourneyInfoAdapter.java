package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.moudle.user.bean.JourneyInfo;
import com.dcch.sharebike.utils.MapUtil;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class JourneyInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private JourneyInfo mInfos;

    public JourneyInfoAdapter(Context context) {
        mContext = context;
    }

    public void setInfos(JourneyInfo infos) {
        mInfos = infos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_journey, parent, false);//这个布局就是一个imageview用来显示图片
        JourneyViewHolder holder = new JourneyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof JourneyViewHolder){
            ((JourneyViewHolder) holder).journeyTime.setText(mInfos.getCarrOrders().get(position).getCarRentalOrderDate());
            ((JourneyViewHolder) holder).bikeNum.setText(mInfos.getCarrOrders().get(position).getBicycleNo());
            ((JourneyViewHolder) holder).duration.setText(String.valueOf(MapUtil.timeFormatter(mInfos.getCarrOrders().get(position).getTripTime())));
            ((JourneyViewHolder) holder).money_amount.setText(String.valueOf(mInfos.getCarrOrders().get(position).getRideCost())+"元");
        }
    }

    @Override
    public int getItemCount() {
        if (mInfos != null) {
            return mInfos.getCarrOrders().size();
        }
        return 0;
    }

    class JourneyViewHolder extends RecyclerView.ViewHolder {
        TextView journeyTime, bikeNum, duration, money_amount;

        public JourneyViewHolder(View itemView) {
            super(itemView);
            journeyTime = (TextView) itemView.findViewById(R.id.journeyTime);
            bikeNum = (TextView) itemView.findViewById(R.id.bikeNum);
            duration = (TextView) itemView.findViewById(R.id.duration);
            money_amount = (TextView) itemView.findViewById(R.id.money_amount);
        }
    }

}
