package com.example.vamsi.firebaseauth1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.fabric.sdk.android.Fabric;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "W3bRMK8RliCpyba6Nq5v0gNql";
    private static final String TWITTER_SECRET = "SYIzuiIsqeiVKRmuaVlfUjRL7sO3W4bEhaLw8goZhUsBWJ3hCz";


    private EditText emaiIDText,passwordText;
    private TextView forgotPassword;
    private Button register,login;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mUser;
    private DatabaseReference rootReference;
    private String emailID,password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If the user is logged in, he should not be shown the login screen again. Hence the code below




        sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);


        getSupportActionBar().show();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_main);

        //Sending html code to textView to make 'click here' underlined
        TextView clickhere = (TextView) findViewById(R.id.clickhere);
        String s = "<u>Click Here<u>";
        clickhere.setText(Html.fromHtml(s));

        //Getting the views from the xml file
        emaiIDText = (EditText)findViewById(R.id.emailID);
        if(!(sharedPreferences.getString("emailID","none").equals("none")))
            emaiIDText.setText(sharedPreferences.getString("emailID","none"));
        passwordText = (EditText)findViewById(R.id.password);

        forgotPassword = (TextView) findViewById(R.id.clickhere);

        register = (Button)findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user == null)
//                    Toast.makeText(MainActivity.this,"Logged Off ",Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(MainActivity.this,"Signed In : "+ user.getEmail(),Toast.LENGTH_SHORT).show();
            }

        };




        //Getting an instance of the firebase Database
        mDataBase = FirebaseDatabase.getInstance();

        //reference to child node users
        mUser = mDataBase.getReference().child("Users");

        //Creating a child listener to 'Users' which triggers on initialization and for every child change event
        ChildEventListener userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        //Attaching the child listener created above to the 'Users' node
        mUser.addChildEventListener(userListener);

        //Getting instance of FirebaseAuth


        register.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                //String userID = mUser.push().getKey();
                //writeNewUser(userID,emailID,password);
                //registerUser();
                Digits.logout();
                    Intent i = new Intent(MainActivity.this,RegisterUser.class);
                    startActivity(i);

            }
        });

        //Sending intent to display user list.

        /*viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,userList.class);
                startActivity(i);
            }
        });*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pd =new ProgressDialog(MainActivity.this);
                pd.setCancelable(true);
                pd.setMax(10);
                pd.setMessage("Signing In ...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                emailID = emaiIDText.getText().toString();
                password = passwordText.getText().toString();

                if(emailID.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter email ID", Toast.LENGTH_SHORT).show();
                }

                else if(password.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    firebaseAuth.signInWithEmailAndPassword(emailID,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        pd.dismiss();

                                        rootReference.child("Users").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                User user = dataSnapshot.getValue(User.class);
                                                if(user.getEmail().equals(emailID))
                                                {
                                                    editor.putString("userID",dataSnapshot.getKey());
                                                    editor.putString("emailID",emailID);
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
                                        //Toast.makeText(MainActivity.this, "Signed In : " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(MainActivity.this, HomeScreen.class);
                                        startActivity(i);
                                    }
                                    else {//Toast.makeText(MainActivity.this,"Failed : "+task.getException(),Toast.LENGTH_SHORT).show();


                                        pd.dismiss();

                                        Intent i = new Intent(MainActivity.this, HomeScreen.class);
                                        startActivity(i);
                                    }
                                }
                            });

                }

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(MainActivity.this,ForgotPassword.class);
                //assuming that the user tried to login in and failed, we are fetching the email that was used in failed attempt
                if(emaiIDText.getText().toString() != null)
                    i.putExtra("emailID",emaiIDText.getText().toString());
                startActivity(i);
                //Fabric initialisation is already done in onCreate() method.
                /*AuthCallback authCallback = new AuthCallback() {
                    @Override
                    public void success(DigitsSession session, String phoneNumber) {
                        Toast.makeText(MainActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this,ForgotPassword.class);
                        startActivity(i);
                    }

                    @Override
                    public void failure(DigitsException error) {
                        Toast.makeText(MainActivity.this, "Verification failed due to "+error, Toast.LENGTH_SHORT).show();
                    }
                };

                rootReference.child("Users").child()

                AuthConfig.Builder builder = new AuthConfig.Builder()
                                                .withAuthCallBack(authCallback).withPhoneNumber()*/





            }
        });


    }

   /* private void writeNewUser(String userID,String emailID,String password)
    {
        User user = new User(emailID,password);
        mUser.child(userID).setValue(user);
    }*/


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            Intent i = new Intent(MainActivity.this,HomeScreen.class);
            startActivity(i);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
