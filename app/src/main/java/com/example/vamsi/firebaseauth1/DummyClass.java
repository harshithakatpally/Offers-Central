package com.example.vamsi.firebaseauth1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vamsi on 25/5/17.
 */

public class DummyClass extends AppCompatActivity {

    private DatabaseReference rootReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dummy_layout);

        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //rootReference.child("Wallets").child(mAuth.getCurrentUser().getUid()).child("WalletDetails").setValue(walletDetails);

        //To create Brands Coupons

    }


    public void buttonOnclick(View v)
    {
        rootReference.child("Images").child("Brands").child("Food").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final CouponBrand2 couponBrand = dataSnapshot.getValue(CouponBrand2.class);
                rootReference.child("Coupons").child(mAuth.getCurrentUser().getUid()).child("Brands").child("Food")
                        .child(couponBrand.getBrandName()).setValue(couponBrand);

                //To create all the coupons of a particular brand
                rootReference.child("Images").child(couponBrand.getBrandName()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        CouponDetails2 couponDetails = dataSnapshot.getValue(CouponDetails2.class);

                        rootReference.child("Coupons").child(mAuth.getCurrentUser().getUid()).child(couponBrand.getBrandName())
                                .child(couponDetails.getCouponId()).setValue(couponDetails);
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


    public void deleteButtonOnClick(View v)
    {
        rootReference.child("Coupons").child(mAuth.getCurrentUser().getUid()).setValue(null);
        rootReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(null);
        rootReference.child("Wallets").child(mAuth.getCurrentUser().getUid()).setValue(null);
        rootReference.child("WishLists").child(mAuth.getCurrentUser().getUid()).setValue(null);


        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DummyClass.this);
                builder.setTitle("Good Bye !!!");
                builder.setMessage("We'll make sure to serve you better");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent i = new Intent(DummyClass.this,MainActivity.class);
                        startActivity(i);
                    }
                });
                builder.show();
            }
        });
    }

}
