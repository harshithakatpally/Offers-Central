package com.example.vamsi.firebaseauth1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by FALCONS on 4/25/2017.
 */

public class WishList extends AppCompatActivity{

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<CouponDetails,CouponDetailsViewHolder> adapter;
    private String smallLogoUrl;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DatabaseReference rootReference,wishListsReference;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist_screen);

        sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recyclerView = (RecyclerView) findViewById(R.id.wishListRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

        rootReference = FirebaseDatabase.getInstance().getReference();
        wishListsReference = rootReference.child("WishLists").child(sharedPreferences.getString("userID","none"));

        adapter = new FirebaseRecyclerAdapter<CouponDetails, CouponDetailsViewHolder>
                (CouponDetails.class,R.layout.coupons_card_view,CouponDetailsViewHolder.class,wishListsReference) {

            @Override
            protected void populateViewHolder(final CouponDetailsViewHolder viewHolder, final CouponDetails model, int position) {
                //TODO Instead of fetching the small logo url from the CouponBrand class object, add it as an attribute to CouponDetails class
                if(model.getCouponId().substring(0,2).equals("DM"))
                {
                    rootReference.child("Images").child("Brands").child("Food").child("2").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CouponBrand2 couponBrand2 = dataSnapshot.getValue(CouponBrand2.class);
                            smallLogoUrl = couponBrand2.getSmallLogoUrl();

                            StorageReference st = FirebaseStorage.getInstance().getReferenceFromUrl(smallLogoUrl);
                            viewHolder.setImageView(getApplicationContext(),st);
                            viewHolder.couponDescription.setText(model.getdescription());
                            viewHolder.couponExpiry.setText(model.getExpiry());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        };

        recyclerView.setAdapter(adapter);
    }
}
