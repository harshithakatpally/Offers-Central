package com.example.vamsi.firebaseauth1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by vamsi on 4/6/17.
 */

public class VerifyOtp extends AppCompatActivity {

    private String mobile,otp;
    private TextView otpMessageTextView;
    private EditText otpEditText;
    private BroadcastReceiver broadcastReceiver;
    private Button verifyButton;
    private boolean otpVerified;
    private DatabaseReference rootReference;
    private FirebaseUser firebaseUser;
    private ProgressDialog pd,progressDialog;
    private Bundle bundle;

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("otp"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.otp);

        bundle = getIntent().getExtras();
        mobile = bundle.getString("mobile");


        otpVerified = false;

        otpMessageTextView = (TextView) findViewById(R.id.message);
        otpMessageTextView.setText("Enter the otp sent to xxxx"+mobile.substring(6));
        otpEditText = (EditText) findViewById(R.id.otp);
        verifyButton = (Button) findViewById(R.id.verify);

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        otp = generateOtp();

        pd = new ProgressDialog(this);
        pd.setMessage("Generating OTP ...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(true);
        pd.show();

        progressDialog = new ProgressDialog(VerifyOtp.this);
        progressDialog.setMessage("Waiting for OTP .....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        RequestQueue requestQueue = Volley.newRequestQueue(VerifyOtp.this);
        String url = "http://smsgateway.me/api/v3/messages/send?email=vamsikrish95@gmail.com&password=prakamsiv95" +
                "&device=48836&number="+mobile+"&message="+"Enter the otp : "+otp+" into Offers Central";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        progressDialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerifyOtp.this, "Failed ! "+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("otp"))
                {
                    pd.dismiss();
                    progressDialog.dismiss();
                    Bundle b = intent.getExtras();
                    otpEditText.setText(b.getString("message"));
                    if(otp.equals(b.getString("message")))
                    {
                        otpVerified = true;

//                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOtp.this);
//                        builder.setTitle("OTP Verification");
//                        builder.setMessage("OTP matched, Mobile Verified !!!");
//                        builder.setCancelable(false);
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                rootReference.child("Users").child(firebaseUser.getUid()).child("mobileVerified").setValue(true);
//
//                                Intent i = new Intent(VerifyOtp.this,VerifyCredentials.class);
//                                startActivity(i);
//                            }
//                        });
//                        builder.show();
                        if(bundle.getString("source") !=null)
                        {
                            if(bundle.getString("source").equals("resetPassword"))
                            {
                                Intent i = new Intent(VerifyOtp.this,ResetPassword.class);
                                i.putExtra("email",bundle.getString("email"));
                                i.putExtra("source","resetPassword");
                                startActivity(i);
                            }
                        }

                        else
                        {
                            rootReference.child("Users").child(firebaseUser.getUid())
                                    .child("mobileVerified").setValue(true);
                            Intent i = new Intent(VerifyOtp.this,VerifyCredentials.class);
                            startActivity(i);
                        }

                    }
                }
            }
        };

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.equals(otpEditText.getText().toString()))
                {
                    otpVerified = true;
                }

                if(!otpVerified)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("OTP Verification");
                    builder.setMessage("OTP entered didn't match, Enter correct Otp");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }

                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOtp.this);
                    builder.setTitle("OTP Verification");
                    builder.setMessage("OTP matched, Mobile Verified !!!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(bundle.getString("source") !=null)
                            {
                                if(bundle.getString("source").equals("resetPassword"))
                                {
                                    Intent i = new Intent(VerifyOtp.this,ResetPassword.class);
                                    i.putExtra("email",bundle.getString("email"));
                                    i.putExtra("source","resetPassword");
                                    startActivity(i);
                                }
                            }

                            else
                            {
                                rootReference.child("Users").child(firebaseUser.getUid())
                                        .child("mobileVerified").setValue(true);
                                Intent i = new Intent(VerifyOtp.this,VerifyCredentials.class);
                                startActivity(i);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

    }


    public String generateOtp()
    {

        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";


        String values = Capital_chars + Small_chars +
                numbers;

        // Using random method
        Random rndm_method = new Random();

        //char[] password = new char[6];
        String password = "";

        for (int i = 0; i < 6; i++) {
            password = password+
                    values.charAt(rndm_method.nextInt(values.length()));
        }

        return password;


    }
}
