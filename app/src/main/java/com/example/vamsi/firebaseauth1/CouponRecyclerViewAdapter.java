package com.example.vamsi.firebaseauth1;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by VAMSI on 26-03-2017.
 */

public class CouponRecyclerViewAdapter extends RecyclerView.Adapter<CouponRecyclerViewAdapter.CouponViewHolder> {

    private List<CouponBrand> couponBrands;

    public CouponRecyclerViewAdapter (List<CouponBrand> couponBrands)
    {
        this.couponBrands = couponBrands;
    }

    public static class CouponViewHolder extends RecyclerView.ViewHolder{

        CardView coupon;
        TextView couponBrand;
        ImageView thumbnail;
        public CouponViewHolder(View couponView) {
            super(couponView);
            coupon = (CardView) couponView.findViewById(R.id.couponCardView);
            couponBrand = (TextView) couponView.findViewById(R.id.couponDescription);
            thumbnail = (ImageView) couponView.findViewById(R.id.thumbnail);
        }
    }

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View couponView = layoutInflater.inflate(R.layout.album_card,null);
        return new CouponViewHolder(couponView);
    }

    @Override
    public void onBindViewHolder(CouponViewHolder holder, int position) {

        holder.thumbnail.setImageResource(couponBrands.get(position).getThumbnailID());
        holder.couponBrand.setText(couponBrands.get(position).getBrandName());
    }



    @Override
    public int getItemCount() {
        return couponBrands.size();
    }
}
