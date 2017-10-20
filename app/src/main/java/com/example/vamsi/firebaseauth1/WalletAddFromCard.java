package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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


public class WalletAddFromCard extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button pay;
    private String month,year,amount,cardNumber,cvv,userID;
    private TextView amountTextView,warning2TextView,warning3TextView;
    private CheckBox saveCardCheckbox;
    private EditText cardNumberEditText,cvvEditText;
    private boolean checked;
    private DatabaseReference rootReference;
    private FirebaseAuth firebaseAuth;
    private User user;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_add_from_card);

        Bundle bundle = getIntent().getExtras();
        amount = bundle.getString("amount");
        //Toast.makeText(this, "Amount : "+amount, Toast.LENGTH_SHORT).show();
        amountTextView = (TextView) findViewById(R.id.amount2);
        amountTextView.setText(amount);

        saveCardCheckbox = (CheckBox) findViewById(R.id.saveCardCheckbox);
        saveCardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    checked = true;
                else
                    checked = false;
            }
        });

        cardNumberEditText = (EditText) findViewById(R.id.cardNumber2);
        cvvEditText = (EditText) findViewById(R.id.cvv);
        warning2TextView = (TextView) findViewById(R.id.warning2);
        warning3TextView = (TextView) findViewById(R.id.warning3);

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        Spinner spinnerm = (Spinner) findViewById(R.id.spinnermonth);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterm = ArrayAdapter.createFromResource(this,
                R.array.Months, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerm.setAdapter(adapterm);
        spinnerm.setOnItemSelectedListener(this);


        Spinner spinnery = (Spinner) findViewById(R.id.spinneryear);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptery = ArrayAdapter.createFromResource(this,
                R.array.Years, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adaptery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnery.setAdapter(adaptery);
        spinnery.setOnItemSelectedListener(this);

        pay = (Button) findViewById(R.id.pay1);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNumber = cardNumberEditText.getText().toString();
                if(cardNumber.equals(""))
                    warning2TextView.setVisibility(View.VISIBLE);

                else
                    warning2TextView.setVisibility(View.GONE);

                cvv = cvvEditText.getText().toString();
                if(cvv.equals(""))
                    warning3TextView.setVisibility(View.VISIBLE);
                else
                    warning3TextView.setVisibility(View.GONE);

                if(!cardNumber.equals("") && !cvv.equals(""))
                {
                    if(checked)
                    {
                        SavedCards savedCards = new SavedCards(cardNumber,month,year,cvv);
                        rootReference.child("Wallets").child(userID).child("SavedCards").push().setValue(savedCards);
                    }

                    rootReference.child("Wallets").child(userID).child("WalletDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final WalletDetails walletDetails = dataSnapshot.getValue(WalletDetails.class);
                            rootReference.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    user = dataSnapshot.getValue(User.class);
                                    Intent i = new Intent(WalletAddFromCard.this,WalletBankScreen.class);
                                    i.putExtra("name",user.getName());
                                    i.putExtra("mobile",user.getMobile());
                                    i.putExtra("password",user.getPassword());
                                    i.putExtra("userID",userID);
                                    i.putExtra("email",user.getEmail());
                                    i.putExtra("amount",amount);
                                    i.putExtra("prevBalance",walletDetails.getBalance());
                                    startActivity(i);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }



            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner)adapterView;
        // On selecting a spinner item
        if(spinner.getId() == R.id.spinnermonth)
            month = spinner.getItemAtPosition(i).toString();
        else
            year = spinner.getItemAtPosition(i).toString();
        //Toast.makeText(this, "Month : "+month+" Year : "+year, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
