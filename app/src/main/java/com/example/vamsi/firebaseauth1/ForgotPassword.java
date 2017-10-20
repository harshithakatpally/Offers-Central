package com.example.vamsi.firebaseauth1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by VAMSI on 07-03-2017.
 */

public class ForgotPassword extends AppCompatActivity {

    private Button verify;
    private static final String TWITTER_KEY = "W3bRMK8RliCpyba6Nq5v0gNql";
    private static final String TWITTER_SECRET = "SYIzuiIsqeiVKRmuaVlfUjRL7sO3W4bEhaLw8goZhUsBWJ3hCz";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    RadioButton currentEmailID,anotherEmailID;
    private EditText anotherEmailIDInput;
    private RelativeLayout messageLayout;
    private TextView verifiedAccountMessage,unverifiedAccountMessage;
    ImageView rightOrWrongImageView;
    private Button submit,check;
    private DatabaseReference rootReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String emailID;
    private boolean isemailVerified;
    private RadioGroup optionsRadioGroup;
    private String mobile;
    private RadioButton emailOptionRadioButton,otpOptionRadioButton;
    private TextView anotherEmailMessage;

    @Override
    protected void onCreate(@Nullable Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.forgot_password);

        sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        currentEmailID = (RadioButton) findViewById(R.id.currentEmailID);
        anotherEmailID = (RadioButton) findViewById(R.id.anotherEmailID);

        messageLayout = (RelativeLayout) findViewById(R.id.messageRelativeLayout);

        anotherEmailIDInput = (EditText) findViewById(R.id.anotherEmailIDInput);

        verifiedAccountMessage = (TextView) findViewById(R.id.verifiedAccountMessage);
        unverifiedAccountMessage = (TextView) findViewById(R.id.unverifiedAccountMessage);
        anotherEmailMessage = (TextView) findViewById(R.id.anotherEmailMessage);

        optionsRadioGroup = (RadioGroup) findViewById(R.id.optionsRadioGroup);
        emailOptionRadioButton = (RadioButton) findViewById(R.id.emailOption);
        otpOptionRadioButton = (RadioButton) findViewById(R.id.otpOption);

        rightOrWrongImageView = (ImageView) findViewById(R.id.rightOrWrongImageView);

        submit = (Button) findViewById(R.id.submit);
        check = (Button) findViewById(R.id.check);

        isemailVerified = false;

        rootReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    firebaseUser = firebaseAuth.getCurrentUser();
                }
                else
                {
                    Toast.makeText(ForgotPassword.this, "Error Signing in : "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET);
        Fabric.with(this,new TwitterCore(authConfig),new Digits.Builder().build());

        verify = (Button) findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPassword.this,ChangePassword.class);
                startActivity(i);
            }
        });*/

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle.getString("emailID") != null)
        {
            //Set this emailID to 1st radio button
            currentEmailID.setText(bundle.getString("emailID"));
        }
        else if(!(sharedPreferences.getString("emailID","none").equals("none")))
            //Set the radio button text to last logged in user
            currentEmailID.setText(sharedPreferences.getString("emailID","none"));
    }

    public void mailChoiceRadioButtonOnClick(View v)
    {

        if(v.getId() == R.id.currentEmailID)
        {
            emailID = currentEmailID.getText().toString();
            messageLayout.setVisibility(View.INVISIBLE);

            isemailVerified = false;
            if(!emailID.equals("") && firebaseUser!=null)
            {

                rootReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                        {
                            User user = userSnapshot.getValue(User.class);

                            mobile = user.getMobile();
                            if(user.getEmail().equals(emailID))
                            {
                                check.setVisibility(View.INVISIBLE);
                                anotherEmailIDInput.setVisibility(View.INVISIBLE);
                                rightOrWrongImageView.setVisibility(View.INVISIBLE);
                                anotherEmailMessage.setVisibility(View.INVISIBLE);
                                messageLayout.setVisibility(View.VISIBLE);
                                submit.setText("Continue");
                                unverifiedAccountMessage.setVisibility(View.INVISIBLE);
                                isemailVerified = true;
                                verifiedAccountMessage.setVisibility(View.VISIBLE);
                                verifiedAccountMessage.setText("A/C exists ! Choose a method for verification : ");
                                optionsRadioGroup.setVisibility(View.VISIBLE);
                                emailOptionRadioButton.setText("Send email to "+user.getEmail());
                                otpOptionRadioButton.setText("Send OTP to xxxx"+user.getMobile().substring(6));
                            }


                        }

                        if(!isemailVerified)
                        {
                            submit.setText("Register");
                            verifiedAccountMessage.setVisibility(View.GONE);
                            rightOrWrongImageView.setVisibility(View.VISIBLE);
                            rightOrWrongImageView.setImageDrawable(getDrawable(R.mipmap.wrong));
                            unverifiedAccountMessage.setVisibility(View.VISIBLE);
                            submit.setText("Register");
                            optionsRadioGroup.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            else
                Toast.makeText(this, "Please enter emailID; then click check", Toast.LENGTH_SHORT).show();

        }

        else if(v.getId() == R.id.anotherEmailID)
        {
            messageLayout.setVisibility(View.VISIBLE);
        }
    }

    public void checkOnClick(View v)
    {
        isemailVerified = false;
        if(anotherEmailIDInput.getText()!=null)
        {
            emailID = anotherEmailIDInput.getText().toString();
            if(!emailID.equals("") && firebaseUser!=null)
            {
                rootReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                        {
                            User user = userSnapshot.getValue(User.class);

                            mobile = user.getMobile();
                              if(user.getEmail().equals(emailID))
                            {
                                submit.setText("Continue");
                                unverifiedAccountMessage.setVisibility(View.INVISIBLE);
                                isemailVerified = true;
                                rightOrWrongImageView.setVisibility(View.VISIBLE);
                                rightOrWrongImageView.setImageDrawable(getDrawable(R.mipmap.check));
                                verifiedAccountMessage.setVisibility(View.VISIBLE);
                                verifiedAccountMessage.setText("A/C exists ! Choose a method for verification : ");
                                optionsRadioGroup.setVisibility(View.VISIBLE);
                                emailOptionRadioButton.setText("Send email to "+user.getEmail());
                                otpOptionRadioButton.setText("Send OTP to xxxx"+user.getMobile().substring(6));
                            }


                        }

                        if(!isemailVerified)
                        {
                            submit.setText("Register");
                            verifiedAccountMessage.setVisibility(View.GONE);
                            rightOrWrongImageView.setVisibility(View.VISIBLE);
                            rightOrWrongImageView.setImageDrawable(getDrawable(R.mipmap.wrong));
                            unverifiedAccountMessage.setVisibility(View.VISIBLE);
                            submit.setText("Register");
                            optionsRadioGroup.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                boolean flag = true;
                if(flag)
                {

                }
                else
                {

                }


            }
        }
        else
            Toast.makeText(this, "Please enter emailID; then click check", Toast.LENGTH_SHORT).show();
    }

    public void submitOnclick(View v)
    {
        Button button = (Button)v;
        if(button.getText().equals("Continue"))
        {
            int id = optionsRadioGroup.getCheckedRadioButtonId();
            if(id == R.id.emailOption)
            {
                firebaseAuth.sendPasswordResetEmail(emailID)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                        builder.setTitle("Attention !!!");
                        builder.setMessage("Password Reset link mailed");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(ForgotPassword.this,MainActivity.class);
                                firebaseAuth.getCurrentUser().delete();
                                firebaseAuth.signOut();
                                startActivity(i);
                            }
                        });
                        builder.show();
                    }
                });


            }

            else if(id == R.id.otpOption)
            {
                Intent i = new Intent(this,VerifyOtp.class);
                i.putExtra("source","resetPassword");
                i.putExtra("email",emailID);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        }

        else if(button.getText().equals("Register"))
        {
            //TODO check if it works properly
            Intent i = new Intent(ForgotPassword.this,RegisterUser.class);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.signOut();
    }
}