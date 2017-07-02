package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.moudle.user.bean.MessageInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MessageInfoAdapter extends CommonAdapter<MessageInfo.ActivitysBean> {
    public MessageInfoAdapter(Context context, int layoutId, List<MessageInfo.ActivitysBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MessageInfo.ActivitysBean activitysBean, int position) {
        holder.setText(R.id.message_theme,activitysBean.getActivityname());
        Glide.with(App.getContext()).load(activitysBean.getActivityimage()).error(R.drawable.sharebike).into((ImageView) holder.getView(R.id.message_image));
    }


}
