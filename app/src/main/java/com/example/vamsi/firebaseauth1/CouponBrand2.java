package com.example.vamsi.firebaseauth1;

import android.content.Context;

/**
 * Created by VAMSI on 30-03-2017.
 */

public class CouponBrand2  {

    private String brandName,url,smallLogoUrl;
    private int couponCount,count,prefCount;

    public CouponBrand2() {}

    public CouponBrand2(String url,String brandName,String smallLogoUrl,int couponCount,int count,int prefCount)
    {
        this.brandName = brandName;
        this.url = url;
        this.smallLogoUrl=smallLogoUrl;
        this.couponCount = couponCount;
        this.count = count;
        this.prefCount = prefCount;
    }

    public String getBrandName(){
        return brandName;
    }

    public String getUrl(){
        return url;
    }

    public  String getSmallLogoUrl(){
        return smallLogoUrl;
    }

    public void setCouponCount(int i)
    {
        couponCount = i;
    }

    public void setCount()
    {
        count++;
    }

    public int getCouponCount(){return couponCount;}

    public  int getCount(){return count;}

    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    public int getPrefCount(){return prefCount;}

    public void setUrl(Context context,String url)
    {

        this.url= url;
    }

}
