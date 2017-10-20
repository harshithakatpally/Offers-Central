package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by FALCONS on 5/15/2017.
 */

public class WalletBankScreen extends AppCompatActivity {

    private WebView webView;
    private String mobile,name,userID,url,password,email,amount,prevBalance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference rootReference;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_bank_screen);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        if(sharedPreferences.getInt("amount",0) != 0)
//        {
//            Intent i = new Intent(this,RedeemCheckout.class);
//            startActivity(i);
//        }

        rootReference = FirebaseDatabase.getInstance().getReference();

        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        mobile = b.getString("mobile");
        userID = b.getString("userID");
        password = b.getString("password");
        email = b.getString("email");
        amount = b.getString("amount");
        prevBalance = b.getString("prevBalance");

        webView = (WebView) findViewById(R.id.webView);
        url = "http://192.168.0.7/firebase.php?mobileNumber="+mobile+"&name="+name+"&userID="+userID+"&password="+password
                +"&email="+email+"&amount="+amount;
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //after getting previous balance,
        rootReference.child("Wallets").child(userID).child("WalletDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WalletDetails walletDetails = dataSnapshot.getValue(WalletDetails.class);
                //Toast.makeText(WalletBankScreen.this, "Prev balance : "+prevBalance+" current balance : "+walletDetails.getBalance(), Toast.LENGTH_SHORT).show();
                if(Integer.valueOf(walletDetails.getBalance())>Integer.valueOf(prevBalance))
                {
                    webView.clearHistory();
                    webView.clearFormData();
                    webView.clearMatches();
                    AlertDialog.Builder builder = new AlertDialog.Builder(WalletBankScreen.this);
                    builder.setTitle("Alert!!!");
                    builder.setMessage("Wallet Recharge Success, balance : "+walletDetails.getBalance());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(sharedPreferences.getInt("amount",0) != 0)
                            {
                                Intent i  = new Intent(WalletBankScreen.this,RedeemCheckout.class);
                                startActivity(i);
                            }

                            else
                            {
                                Intent i = new Intent(WalletBankScreen.this,HomeScreen.class);
                                startActivity(i);
                            }
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
