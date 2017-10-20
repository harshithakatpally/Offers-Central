package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by VAMSI on 31-03-2017.
 */

public class CouponLayoutSingle extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DatabaseReference rootReference;
    String couponID;
    public Button redeem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_layout_single);

        ImageView couponImage = (ImageView) findViewById(R.id.couponImage);
        TextView terms = (TextView) findViewById(R.id.termsConditionsTextArea);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        rootReference = FirebaseDatabase.getInstance().getReference();

        terms.setMovementMethod(new ScrollingMovementMethod());
        Bundle b = getIntent().getExtras();
        String couponDescription = b.getString("name");
        String couponTerms = b.getString("terms");
        String couponUrl = b.getString("url");
        couponID = b.getString("couponID");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(couponUrl);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(couponImage);
        terms.setText(couponTerms);


        redeem = (Button) findViewById(R.id.redeemCoupon);

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent r = new Intent(getApplicationContext(), RedeemCoupon.class);
                r.putExtra("source","fromcoupon");
                r.putExtra("couponID",couponID);
                startActivity(r);
            }
        });
    }

    public void onAddToWishListClick(View v)
    {

            //TODO Add to wishlist
//        rootReference.child("Images").child(sharedPreferences.getString("brandName","none"))
//                .addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                CouponDetails2 couponDetails = dataSnapshot.getValue(CouponDetails2.class);
//                {
//                    if(couponDetails.getCouponId().equals(sharedPreferences.getString("couponID","none")))
//                    {
//                        //Toast.makeText(CouponLayoutSingle.this, "Clicked CouponID : "+couponDetails.getCouponId(), Toast.LENGTH_SHORT).show();
//                        rootReference.child("WishLists").child(sharedPreferences.getString("userID","none"))
//                                .child(sharedPreferences.getString("couponID","none")).setValue(couponDetails);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        //rootReference.child("Coupons").child(sharedPreferences.getString("userID","none"))
    }
}
