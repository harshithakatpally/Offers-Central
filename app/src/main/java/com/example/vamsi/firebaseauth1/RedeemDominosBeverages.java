package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by FALCONS on 5/22/2017.
 */

public class RedeemDominosBeverages extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button add;
    Button more;
    Spinner beverages;
    String select1, select2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_dominos_beverages);

        Spinner spinnerb = (Spinner) findViewById(R.id.beverages);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterb = ArrayAdapter.createFromResource(this,
                R.array.beverages, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerb.setAdapter(adapterb);
        spinnerb.setOnItemSelectedListener(this);

        Spinner spinnerb1 = (Spinner) findViewById(R.id.beverages1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterb1 = ArrayAdapter.createFromResource(this,
                R.array.beverages, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterb1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerb1.setAdapter(adapterb1);
        spinnerb1.setOnItemSelectedListener(this);


        add = (Button) findViewById(R.id.additems);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rc = new Intent(getApplicationContext(), RedeemCoupon.class);
                rc.putExtra("source","frombeverages");
                if(select1!=null){
                    RedeemCoupon.items.add(select1);
                }

                if(select2!=null){
                    RedeemCoupon.items.add(select2);
                }
                startActivity(rc);
            }
        });

        more = (Button) findViewById(R.id.morebeverages);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beverages= (Spinner) findViewById(R.id.beverages1);
                beverages.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // On selecting a spinner item
        switch (adapterView.getId()){
            case R.id.beverages:
                String item = adapterView.getItemAtPosition(i).toString();
                if (!item.equals("None")) {
                    select1 = item;
                }
                // Showing selected spinner item
                break;
            case R.id.beverages1:
                String item1 = adapterView.getItemAtPosition(i).toString();
                if (!item1.equals("None")) {
                    select2 = item1;
                }
                // Showing selected spinner item
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
