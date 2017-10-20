package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by FALCONS on 5/21/2017.
 */

public class RedeemDominosDips extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button add;
    Button more;
    Spinner dips;
    String select1, select2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_dominos_dips);

        Spinner spinnerd = (Spinner) findViewById(R.id.dips);
        spinnerd.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterd = ArrayAdapter.createFromResource(this,
                R.array.dips, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerd.setAdapter(adapterd);

        Spinner spinnerd1 = (Spinner) findViewById(R.id.dips1);
        spinnerd1.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterd1 = ArrayAdapter.createFromResource(this,
                R.array.dips, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterd1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerd1.setAdapter(adapterd1);


        add = (Button) findViewById(R.id.additems);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rc = new Intent(getApplicationContext(), RedeemCoupon.class);
                rc.putExtra("source","fromdips");
                if(select1!=null){
                    RedeemCoupon.items.add(select1);
                }

                if(select2!=null){
                    RedeemCoupon.items.add(select2);
                }
                startActivity(rc);
            }
        });

        more = (Button) findViewById(R.id.moredips);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dips= (Spinner) findViewById(R.id.dips1);
                dips.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // On selecting a spinner item
        switch (adapterView.getId()){
            case R.id.dips:
                String item = adapterView.getItemAtPosition(i).toString();
                if (!item.equals("None")) {
                    select1 = item;
                }
                // Showing selected spinner item
                break;
            case R.id.dips1:
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
