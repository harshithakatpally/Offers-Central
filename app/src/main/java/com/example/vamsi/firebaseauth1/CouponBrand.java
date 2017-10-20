package com.example.vamsi.firebaseauth1;

import android.widget.ImageView;

/**
 * Created by VAMSI on 26-03-2017.
 */

public class CouponBrand {


    private int thumbnailID;
    private String brandName;

    public CouponBrand(){}

    public CouponBrand(String brandName, int thumbnailID)
    {
        this.thumbnailID = thumbnailID;
        this.brandName = brandName;
    }

    public int getThumbnailID()
    {
        return thumbnailID;
    }

    public String getBrandName()
    {
        return brandName;
    }
}
