package com.example.vamsi.firebaseauth1.CouponsTabClasses;


//import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vamsi.firebaseauth1.CouponBrand2;
import com.example.vamsi.firebaseauth1.CouponViewHolder2;
import com.example.vamsi.firebaseauth1.CouponsScreen;
import com.example.vamsi.firebaseauth1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by VAMSI on 18-03-2017.
 */

public class MoviesTab extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    RecyclerView recyclerView;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference rootReference;
    FirebaseAuth firebaseAuth;
    DatabaseReference Brands;
    long brandsCount;
    String brandName = "";
    String brandKey;
    CouponBrand2 couponBrand2;
    FirebaseRecyclerAdapter<CouponBrand2,CouponViewHolder2> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.home_screen_coupon_brands,container,false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),1));

        firebaseStorage = FirebaseStorage.getInstance();

        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseDatabase = FirebaseDatabase.getInstance();

        //Brands = firebaseDatabase.getReference().child("Coupons/Brands/Food");

        Brands = firebaseDatabase.getReference().child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child("Brands/Movies");

        Query brandsQuery = Brands.orderByChild("prefCount");

        adapter = new FirebaseRecyclerAdapter<CouponBrand2, CouponViewHolder2>
                (CouponBrand2.class,R.layout.home_screen_coupon_card,CouponViewHolder2.class,brandsQuery) {
            @Override
            protected void populateViewHolder(CouponViewHolder2 viewHolder, final CouponBrand2 model, int position) {
                StorageReference st = firebaseStorage.getReferenceFromUrl(model.getUrl());
                viewHolder.setImageView(getActivity(),st);
                viewHolder.couponName.setText(model.getBrandName());

                brandName = model.getBrandName();
                setCouponCount(brandName);

                viewHolder.couponCount.setText(String.valueOf(model.getCouponCount()));

                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent i = new Intent(getActivity().getApplicationContext(), CouponsScreen.class);
                        //TODO remove the parameters passed through intents as We are setting the values in shared preferences

                        setCount(model.getBrandName());



                        i.putExtra("brandName",model.getBrandName());
                        editor.putString("brandName",model.getBrandName());
                        i.putExtra("smallLogoUrl",model.getSmallLogoUrl());
                        editor.putString("smallLogoUrl",model.getSmallLogoUrl());
                        editor.commit();

                        //Toast.makeText(getActivity().getApplicationContext(), "BrandName : "+model.getBrandName(), Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                });
            }
        };



        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return  v;
    }

    //To set the number of coupons for each brand coupon
    //I am going to the brand branch (say Dominoes), counting the number of coupons and setting the value in "Brands/Food/Dominoes(say)" object
    private void setCouponCount(String brand)
    {
        final String brandName = brand;
        //String brandPath = "Coupons/"+firebaseAuth.getCurrentUser().getUid()+"/"+
        final DatabaseReference brandReference  = rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid())
                .child(brand);
        brandReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long count = dataSnapshot.getChildrenCount();
                //String path = "Images/Brands/Food/";
                String path = "Coupons/"+firebaseAuth.getCurrentUser().getUid()+"/"+"Brands/Movies";
                final DatabaseReference databaseReference = firebaseDatabase.getReference(path);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            brandsCount = dataSnapshot.getChildrenCount();
                            couponBrand2 = ds.getValue(CouponBrand2.class);
                            if(couponBrand2.getBrandName().equals(brandName))
                            {
                                brandKey = ds.getKey();
                                databaseReference.child(brandKey).child("couponCount").setValue(count);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setCount(String brand)
    {
        final String brandName = brand;
        Brands.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CouponBrand2 couponBrand2 = dataSnapshot.getValue(CouponBrand2.class);
                if(couponBrand2.getBrandName().equals(brandName))
                    Brands.child(dataSnapshot.getKey()).child("count").setValue(couponBrand2.getCount()+1);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        setCouponCount(brandName);
        Query brandOrder = Brands.orderByChild("prefCount");
        adapter = new FirebaseRecyclerAdapter<CouponBrand2, CouponViewHolder2>
                (CouponBrand2.class,R.layout.home_screen_coupon_card,CouponViewHolder2.class,brandOrder) {
//            @Override
//            public CouponBrand2 getItem(int position) {
//                return super.getItem((getItemCount()-position)-1);
//            }

            @Override
            protected void populateViewHolder(CouponViewHolder2 viewHolder, final CouponBrand2 model, int position) {
                StorageReference st = firebaseStorage.getReferenceFromUrl(model.getUrl());
                viewHolder.setImageView(getActivity(),st);
                viewHolder.couponName.setText(model.getBrandName());

                brandName = model.getBrandName();
                setCouponCount(brandName);

                viewHolder.couponCount.setText(String.valueOf(model.getCouponCount()));


                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setCount(model.getBrandName());
                        Intent i = new Intent(getActivity().getApplicationContext(), CouponsScreen.class);

                        i.putExtra("brandName",model.getBrandName());
                        i.putExtra("smallLogoUrl",model.getSmallLogoUrl());

                        editor.putString("brandName",model.getBrandName());
                        editor.putString("smallLogoUrl",model.getSmallLogoUrl());
                        editor.commit();

                        //Toast.makeText(getActivity().getApplicationContext(), "BrandName :"+model.getBrandName(), Toast.LENGTH_SHORT).show();

                        startActivity(i);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);

    }
}
