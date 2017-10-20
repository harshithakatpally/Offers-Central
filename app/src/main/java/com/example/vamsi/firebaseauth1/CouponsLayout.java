package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by VAMSI on 10-03-2017.
 */

public class CouponsLayout extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupons_layout);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        Bundle b = getIntent().getExtras();
        final String brandName = b.getString("brandName");
        editor.putString("brandName",b.getString("brandName"));
        editor.commit();
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "brandName : "+sharedPreferences.getString("brandName","none"), Toast.LENGTH_SHORT).show();

        final String smallLogoUrl = b.getString("smallLogoUrl");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String brandPath = "Images/"+brandName;


        //Toast.makeText(this,"Brand Path Name : "+brandPath, Toast.LENGTH_SHORT).show();


        final DatabaseReference couponBrandReference = firebaseDatabase.getReference().child(brandPath);

        FirebaseRecyclerAdapter<CouponDetails,CouponDetailsViewHolder> adapter;

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

        adapter = new FirebaseRecyclerAdapter<CouponDetails, CouponDetailsViewHolder>
                (CouponDetails.class,R.layout.coupons_card_view,CouponDetailsViewHolder.class,couponBrandReference) {
            @Override
            protected void populateViewHolder(CouponDetailsViewHolder viewHolder, final CouponDetails model, int position) {
                StorageReference st = firebaseStorage.getReferenceFromUrl(smallLogoUrl);
                viewHolder.setImageView(getApplicationContext(),st);
                viewHolder.couponDescription.setText(model.getdescription());
                viewHolder.couponExpiry.setText(model.getExpiry());


                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        couponBrandReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getValue(CouponDetails.class).getCouponId().equals(model.getCouponId())) {
                                    editor.putString("couponKey",dataSnapshot.getKey());
                                    editor.putString("couponID",model.getCouponId());
                                    editor.commit();
                                }
                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Intent i = new Intent(getApplicationContext(), CouponLayoutSingle.class);

                        i.putExtra("brandName",model.getdescription());
                        i.putExtra("terms",model.getTerms());
                        i.putExtra("url",model.getUrl());
                        startActivity(i);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();


    }
}
