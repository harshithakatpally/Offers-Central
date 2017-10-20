package com.example.vamsi.firebaseauth1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by VAMSI on 07-03-2017.
 */

public class RegisterUser extends AppCompatActivity {

    private ImageButton viewPassword,viewConfirmPassword;
    private EditText password,confirmPassword,emailID,name,mobile;
    private int count,countcp,digitsSignInCount=0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button register,s;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference,rootReference;

    @Override
    protected void onCreate(@Nullable Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.register_user);

        viewPassword = (ImageButton) findViewById(R.id.viewPassword);
        viewConfirmPassword = (ImageButton) findViewById(R.id.viewConfirmPassword);
        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        emailID = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        register = (Button) findViewById(R.id.register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");
        rootReference = FirebaseDatabase.getInstance().getReference();



        //Setting onClickListener to viewPassword to show and hide text on button clicks
        viewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                //For Odd clicks we have show the text, hence passing null in the transformation method
                // and setting hide icon in the image button
                if(count%2==1) {
                    password.setTransformationMethod(null);
                    viewPassword.setImageDrawable(getResources().getDrawable(R.drawable.hide_password));
                }
                //For even clicks, we need to hide the text, hence enabling the transformation method
                // by passing a new instance as parameter and and setting the show password icon in imageButton
                else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    viewPassword.setImageDrawable(getResources().getDrawable(R.drawable.show_password1));
                }
            }
        });

        viewConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countcp++;
                if(countcp%2==1) {
                    confirmPassword.setTransformationMethod(null);
                    viewConfirmPassword.setImageDrawable(getResources().getDrawable(R.drawable.hide_password));
                }
                else {
                    confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    viewConfirmPassword.setImageDrawable(getResources().getDrawable(R.drawable.show_password1));
                }
            }
        });

        //Initializing the mAuth with the current instance
        mAuth = FirebaseAuth.getInstance();

        //listening to auth changes using AuthStateListener object

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Creating Account ....");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pass = password.getText().toString();
                final String email = emailID.getText().toString();
                final String uname = name.getText().toString();
                final String mobileno = mobile.getText().toString();
                String cPassword = confirmPassword.getText().toString();



                if(pass.equals(cPassword)) {



                    AuthCallback authCallback = new AuthCallback() {
                        @Override
                        public void success(DigitsSession session, String phoneNumber) {



                        }

                        @Override
                        public void failure(DigitsException error) {
                            Toast.makeText(RegisterUser.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    };



                    progressDialog.show();

                    final User user = new User(uname, email, pass, mobileno,false,false);
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                progressDialog.dismiss();

                                userReference.child(mAuth.getCurrentUser().getUid()).setValue(user);

                                Calendar calendar = Calendar.getInstance();
                                String date = calendar.get(Calendar.DATE)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR);
                                WalletDetails walletDetails = new WalletDetails("200",date);
                                rootReference.child("Wallets").child(mAuth.getCurrentUser().getUid()).child("WalletDetails").setValue(walletDetails);

                                //To create Brands Coupons
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



                                rootReference.child("Images").child("Brands").child("Movies").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        final CouponBrand2 couponBrand = dataSnapshot.getValue(CouponBrand2.class);
                                        rootReference.child("Coupons").child(mAuth.getCurrentUser().getUid()).child("Brands").child("Movies")
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


                                rootReference.child("Images").child("Brands").child("Shopping").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        final CouponBrand2 couponBrand = dataSnapshot.getValue(CouponBrand2.class);
                                        rootReference.child("Coupons").child(mAuth.getCurrentUser().getUid()).child("Brands").child("Shopping")
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


//                                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        Intent i = new Intent(RegisterUser.this,VerifyCredentials.class);
//                                        startActivity(i);
//                                    }
//                                });
                                Intent i = new Intent(RegisterUser.this,VerifyCredentials.class);
                                startActivity(i);






                            } else
                                Toast.makeText(RegisterUser.this, "Exception : " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    //progressDialog.show();
                }
                else Toast.makeText(RegisterUser.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
