package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by FALCONS on 5/15/2017.
 */

public class WalletHomeScreen extends AppCompatActivity {

    private DatabaseReference rootReference;
    private FirebaseAuth firebaseAuth;
    private TextView balanceTextView;
    private TextView lastUpdatedTextView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    Button b1;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_home_screen);

        balanceTextView = (TextView) findViewById(R.id.balance);
        lastUpdatedTextView = (TextView) findViewById(R.id.lastUpdated);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        rootReference.child("Wallets").child(firebaseAuth.getCurrentUser().getUid()).child("WalletDetails")
             .addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     WalletDetails walletDetails = dataSnapshot.getValue(WalletDetails.class);
                     balanceTextView.setText("Rs. " + walletDetails.getBalance() + "/-");
                     editor.putString("walletBalance", walletDetails.getBalance());
                     editor.commit();
                     lastUpdatedTextView.setText("Last Updated : " + walletDetails.getLastUpdated());
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

        b1= (Button) findViewById(R.id.addmoney);
        b1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), WalletSelectMode.class);
                startActivity(i);
            }
        });
    }
}
