package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class WalletSelectMode extends AppCompatActivity {
    private Button debit,credit;
    private EditText cvvEditText,amountEditText;
    private TextView cardNumber,warning1TextView;
    private RelativeLayout savedCard;
    private DatabaseReference savedCardsReference;
    private FirebaseAuth firebaseAuth;
    private SavedCards savedCards;
    private String cvv,amount;
    private Button removeSavedCard;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_select_mode);

        debit= (Button) findViewById(R.id.debitcard);
        credit = (Button) findViewById(R.id.creditcard);

        cvvEditText = (EditText) findViewById(R.id.cvv);
        amountEditText = (EditText) findViewById(R.id.amount);

        cardNumber = (TextView) findViewById(R.id.cardNumber);
        warning1TextView = (TextView) findViewById(R.id.warning1);

        savedCard = (RelativeLayout) findViewById(R.id.savedCard);

        firebaseAuth = FirebaseAuth.getInstance();
        removeSavedCard = (Button) findViewById(R.id.removeSavedCard);

//        amountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus)
//                {
//                    amount = amountEditText.getText().toString();
//                    if(amount.equals(""))
//                    {
//                        warning1TextView.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        warning1TextView.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });

        savedCardsReference = FirebaseDatabase.getInstance().getReference().child("Wallets")
                                .child(firebaseAuth.getCurrentUser().getUid()).child("SavedCards");

        savedCardsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    for(DataSnapshot card : dataSnapshot.getChildren())
                    {
                        savedCards = card.getValue(SavedCards.class);
                        cardNumber.setText(savedCards.getCardNumber());
                    }
                    savedCard.setVisibility(View.VISIBLE);
                }
                else
                    savedCard.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount = amountEditText.getText().toString();
                //Toast.makeText(WalletSelectMode.this, "Amount : "+amount, Toast.LENGTH_SHORT).show();
                if(!amount.equals(""))
                {
                    warning1TextView.setVisibility(View.GONE);
                    Intent i = new Intent(WalletSelectMode.this,WalletAddFromCard.class);
                    i.putExtra("amount",amount);
                    startActivity(i);
                }
                else
                {
                    warning1TextView.setVisibility(View.VISIBLE);
                    Toast.makeText(WalletSelectMode.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = amountEditText.getText().toString();
                //Toast.makeText(WalletSelectMode.this, "Amount : "+amount, Toast.LENGTH_SHORT).show();
                if(!amount.equals(""))
                {
                    warning1TextView.setVisibility(View.GONE);
                    Intent i = new Intent(WalletSelectMode.this,WalletAddFromCard.class);
                    i.putExtra("amount",amount);
                    startActivity(i);
                }
                else
                {
                    warning1TextView.setVisibility(View.VISIBLE);
                    Toast.makeText(WalletSelectMode.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        removeSavedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalletSelectMode.this, "Clicked Remove", Toast.LENGTH_SHORT).show();
                savedCardsReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        SavedCards sc = dataSnapshot.getValue(SavedCards.class);
                        if(sc.getCardNumber().equals(savedCards.getCardNumber()))
                        {
                            savedCardsReference.child(dataSnapshot.getKey()).setValue(null);
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
            }
        });

    }

    public void payOnclick (View v)
    {
        cvv = cvvEditText.getText().toString();
        amount = amountEditText.getText().toString();

        if(cvv != "" && amount!="")
        {
            if(cvv.equals(savedCards.getCvv()))
            {
                Toast.makeText(this, "Valid Credentials", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Invalid CVV, Please Retry", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Please enter cvv before clicking Pay", Toast.LENGTH_SHORT).show();
    }
}
