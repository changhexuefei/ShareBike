package com.dcch.sharebike.moudle.user.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dcch.sharebike.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by gao on 2017/2/19.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;

    final static int TYPE_ADD = 1;
    final static int TYPE_PHOTO = 2;

    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
//                itemView = inflater.inflate(me.iwf.PhotoPickerDemo.R.layout.item_add, parent, false);
                break;
            case TYPE_PHOTO:
//                itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PHOTO) {
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

//            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

//            if (canLoadImage) {
//                Glide.with(mContext)
//                        .load(uri)
//                        .centerCrop()
//                        .thumbnail(0.1f)
//                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
//                        .error(R.drawable.__picker_ic_broken_image_black_48dp)
//                        .into(holder.ivPhoto);
//            }
        }



    }

    @Override
    public int getItemCount() {
        int count = photoPaths.size() + 1;

        return count;
    }

    public static class PhotoViewHolder  extends RecyclerView.ViewHolder{
        private ImageView ivPhoto;
        private View vSelected;
        public PhotoViewHolder(View itemView) {

            super(itemView);
            ivPhoto   = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            if (vSelected != null) vSelected.setVisibility(View.GONE);
        }
    }
}
