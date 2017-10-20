package com.example.vamsi.firebaseauth1;

/**
 * Created by VAMSI on 31-03-2017.
 */

public class CouponDetails {

    public String description;
    public String expiry;
    public String url, terms, couponCode, couponId,keyword;
    int count,minAmount;
    boolean wishlist=false;
    boolean reminder=false;
    boolean nevershow = false;
    int i;
    String couponKeys[] = new String[100];

    public CouponDetails() {

    }


    public CouponDetails(String expiry,String description,String url,String terms,String couponId,String couponCode,
                         int count,boolean wishlist, boolean reminder, boolean nevershow,String keyword,int minAmount)
    {

        this.description = description;
        this.expiry = expiry;
        this.url = url;
        this.terms = terms;

        this.count = count;
        this.couponCode = couponCode;
        this.couponId = couponId;
        this.wishlist = wishlist;
        this.reminder = reminder;
        this.nevershow = nevershow;
        this.keyword = keyword;
        this.minAmount = minAmount;


    }

    public String getdescription() {
        return description;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getUrl() {
        return url;
    }

    public String getTerms() {
        return terms;
    }


    public String getCouponCode() {
        return couponCode;
    }

    public String getCouponId() {
        return couponId;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public boolean isWishlist(){ return  wishlist; }

    public boolean isReminder(){ return  reminder; }

    public boolean isNevershow(){ return  nevershow; }



    public void copyFrom(CouponDetails couponDetails)
    {
        this.description = couponDetails.description;
        this.expiry = couponDetails.expiry;
        this.url = couponDetails.url;
        this.terms = couponDetails.terms;
        this.couponId = couponDetails.couponId;
        this.couponCode = couponDetails.couponCode;
        this.count = couponDetails.count;
    }

    public int getCount(){return count;}

    public void setCouponKey(String couponKey)
    {
        couponKeys[i++] = couponKey;
    }

}
