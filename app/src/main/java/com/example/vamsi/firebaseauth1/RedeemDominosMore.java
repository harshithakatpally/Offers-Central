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

public class RedeemDominosMore extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button add;
    Button more;
    Spinner moreitems;
    String select1, select2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_dominos_more);

        Spinner spinnerm = (Spinner) findViewById(R.id.more);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterm = ArrayAdapter.createFromResource(this,
                R.array.more, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerm.setAdapter(adapterm);
        spinnerm.setOnItemSelectedListener(this);

        Spinner spinnerm1 = (Spinner) findViewById(R.id.more1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterm1 = ArrayAdapter.createFromResource(this,
                R.array.more, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterm1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerm1.setAdapter(adapterm1);
        spinnerm1.setOnItemSelectedListener(this);

        add = (Button) findViewById(R.id.additems);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rc = new Intent(getApplicationContext(), RedeemCoupon.class);
                rc.putExtra("source","frommore");
                if(select1!=null){
                    RedeemCoupon.items.add(select1);
                }

                if(select2!=null){
                    RedeemCoupon.items.add(select2);
                }
                startActivity(rc);
            }
        });

        more = (Button) findViewById(R.id.moremore);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreitems= (Spinner) findViewById(R.id.more1);
                moreitems.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // On selecting a spinner item
        switch (adapterView.getId()){
            case R.id.more:
                String item = adapterView.getItemAtPosition(i).toString();
                if (!item.equals("None")) {
                    select1 = item;
                }
                // Showing selected spinner item
                break;
            case R.id.more1:
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
