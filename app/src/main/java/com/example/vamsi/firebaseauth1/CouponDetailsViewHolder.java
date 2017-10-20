package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

/**
 * Created by VAMSI on 31-03-2017.
 */

public class CouponDetailsViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView couponDescription;
    public TextView couponExpiry;
    public View v;
    public ImageButton imageButton;
    public ImageView dotsImageView;
    public CouponDetailsViewHolder(View itemView) {
        super(itemView);
        v=itemView;
        imageView= (ImageView) v.findViewById(R.id.thumbnail);
        couponDescription= (TextView) v.findViewById(R.id.couponDescription);
        couponExpiry= (TextView) v.findViewById(R.id.couponExpiry);
        dotsImageView = (ImageView) v.findViewById(R.id.dots);
    }

    public void setImageView(Context context, StorageReference st) {
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(st)
                .into(imageView);
    }
}
