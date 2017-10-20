package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

/**
 * Created by VAMSI on 30-03-2017.
 */

public class CouponViewHolder2 extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView couponName;
    public TextView couponCount;
    public ImageView img_dots;
    public View v;
    public CouponViewHolder2(View itemView) {
        super(itemView);
        v = itemView;
        imageView = (ImageView) v.findViewById(R.id.thumbnail);
        couponName = (TextView) v.findViewById(R.id.couponDescription);
        couponCount = (TextView) v.findViewById(R.id.couponCount);
        img_dots = (ImageView) v.findViewById(R.id.dots);
    }

    public void setImageView(Context context, StorageReference storageReference)
    {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);
    }


}
