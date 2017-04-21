package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.dcch.sharebike.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class SetAddressAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SuggestionResult.SuggestionInfo> allSuggestions;

    public SetAddressAdapter(Context context) {
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setAllSuggestions(List<SuggestionResult.SuggestionInfo> allSuggestions) {
        this.allSuggestions = allSuggestions;
    }

    @Override
    public int getCount() {
        if(allSuggestions!=null){
            return allSuggestions.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return allSuggestions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder= new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.item_seek_result,null);
            holder.iv_icon= (ImageView) view.findViewById(R.id.icon_location);
            holder.place_name= (TextView) view.findViewById(R.id.place_name);
            holder.address= (TextView) view.findViewById(R.id.address);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.place_name.setText(allSuggestions.get(i).key);
        holder.address.setText(allSuggestions.get(i).city+allSuggestions.get(i).district);
        return view;
    }

    private class ViewHolder {
        ImageView iv_icon;
        TextView place_name,address;
    }

}
