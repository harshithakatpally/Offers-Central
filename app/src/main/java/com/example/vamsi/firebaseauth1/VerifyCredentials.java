package com.example.vamsi.firebaseauth1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * Created by vamsi on 4/6/17.
 */

public class VerifyCredentials extends AppCompatActivity {

    private TextView userNameTextView,emailVerifyMessageTextView,mobileVerifyMessageTextView;
    private ImageView emailVerifyIcon,mobileVerifyIcon;
    private Button continueButton;
    private DatabaseReference rootReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private User user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean emailVerified,mobileVerified;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verify_credentials);

        userNameTextView = (TextView) findViewById(R.id.verifyUserName);
        emailVerifyMessageTextView = (TextView) findViewById(R.id.emailVerifyMessage);
        mobileVerifyMessageTextView = (TextView) findViewById(R.id.mobileVerifyMessage);
        emailVerifyIcon = (ImageView) findViewById(R.id.emailVerifyIcon);
        mobileVerifyIcon = (ImageView) findViewById(R.id.mobileVerifyIcon);

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyCredentials.this,PreferencesScreen.class);
                startActivity(i);
            }
        });

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(firebaseAuth.getCurrentUser().isEmailVerified())
//                {
//
//
//
//                }
//            }
//        };
//
//        firebaseAuth.addAuthStateListener(authStateListener);



    }




    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser.reload();
        rootReference.child("Users").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        userNameTextView.setText(user.getName());
                        emailVerifyMessageTextView.setText("Email ID :  "+user.getEmail());
                        mobileVerifyMessageTextView.setText("Mobile :  "+user.getMobile());
                        if(!firebaseUser.isEmailVerified())
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VerifyCredentials.this);
                            builder.setTitle("Attention !!!");
                            builder.setMessage("Please verify the confirmation mail sent to "+firebaseUser.getEmail());
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(VerifyCredentials.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            builder.show();
                        }

                        else
                        {
                            rootReference.child("Users").child(firebaseUser.getUid()).child("emailVerified").setValue(true);
                            emailVerifyIcon.setImageDrawable(getDrawable(R.drawable.ic_check_circle_teal_a700_24dp));

                            if(!user.isMobileVerified())
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerifyCredentials.this);
                                builder1.setTitle("Attention !!!");
                                builder1.setMessage("Thanks for verifying Email ! Please verify your mobile number");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        final Intent i = new Intent(VerifyCredentials.this,VerifyOtp.class);
                                        i.putExtra("mobile",user.getMobile());

                                        firebaseAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword())
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            startActivity(i);
                                                        }
                                                    }
                                                });
                                        startActivity(i);
                                        //Intent i = new Intent(VerifyCredentials.this,)

                                    }
                                });
                                builder1.show();
                            }

                            else
                            {
                                continueButton.setVisibility(View.VISIBLE);
                                mobileVerifyIcon.setImageDrawable(getDrawable(R.drawable.ic_check_circle_teal_a700_24dp));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}


