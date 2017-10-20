package com.example.vamsi.firebaseauth1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by FALCONS on 5/25/2017.
 */

public class RedeemCheckout extends AppCompatActivity {


    private TextView balancetv;
    private  TextView amount;
    private Button confirm;
    private int sum;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String walletBalance;
    private Calendar calendar;
    private DatabaseReference rootReference;
    private FirebaseAuth firebaseAuth;
    private int balance;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_checkout);

        balancetv = (TextView) findViewById(R.id.balance);
        amount = (TextView) findViewById(R.id.amount);
        confirm = (Button) findViewById(R.id.confirm);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle b = getIntent().getExtras();
        if(b!=null) {
            sum = b.getInt("amount");
            amount.setText(Integer.toString(sum));
        }

        else
        {
            sum = sharedPreferences.getInt("amount",0);
            editor.putInt("amount",0);
            editor.apply();
            if(sum!=0)
                amount.setText(String.valueOf(sum));
        }

        rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.child("Wallets").child(firebaseAuth.getCurrentUser().getUid()).child("WalletDetails").child("balance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        balancetv.setText(dataSnapshot.getValue(String.class));
                        walletBalance = dataSnapshot.getValue(String.class);

                        if (sum<Integer.valueOf(walletBalance)) {

                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    balance = Integer.parseInt(walletBalance) - sum;

                                    editor.putString("walletBalance", Integer.toString(balance));
                                    rootReference.child("Wallets").child(sharedPreferences.getString("userID", "none")).child("WalletDetails").child("balance").setValue(Integer.toString(balance));

                                    calendar = Calendar.getInstance();
                                    String time = calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
                                    rootReference.child("Wallets").child(sharedPreferences.getString("userID", "none")).child("WalletDetails").child("lastUpdated").setValue(time);


                                    AlertDialog.Builder builder = new AlertDialog.Builder(RedeemCheckout.this);
                                    builder.setTitle("Success Message");
                                    builder.setCancelable(false);
                                    builder.setMessage("Transaction successful , current wallet balance : " + Integer.toString(balance));
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(RedeemCheckout.this, HomeScreen.class);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.show();
                                }
                            });
                        }

                        else{

                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(RedeemCheckout.this);
                            alertdialog.setTitle("Attention!!!");
                            alertdialog.setCancelable(false);
                            alertdialog.setMessage("Insufficient balance. Click OK to recharge.");
                            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(RedeemCheckout.this, WalletHomeScreen.class);
                                    editor.putInt("amount",sum);
                                    editor.apply();
                                    startActivity(intent);
                                }
                            });
                            alertdialog.show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        


    }
}
