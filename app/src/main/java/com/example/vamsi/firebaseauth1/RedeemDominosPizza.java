package com.example.vamsi.firebaseauth1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.vamsi.firebaseauth1.RedeemCoupon.items;
import static com.example.vamsi.firebaseauth1.RedeemCoupon.pizzaSize;

/**
 * Created by FALCONS on 5/21/2017.
 */

public class RedeemDominosPizza extends AppCompatActivity implements AdapterView.OnItemSelectedListener{



    Button add;
    Button more;
    RelativeLayout moreitems;
    private String item="a",item1="a",item2="a",item3="a";
    private String size="a",size1="a",size2="a",size3="a";
    private String source;
    private Spinner spinnervs,spinnernvs,spinnervt,spinnernvt;
    private RadioButton veg,nonVeg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_dominos_pizza);


        add = (Button) findViewById(R.id.additems);
        more = (Button) findViewById(R.id.morepizza);

        veg = (RadioButton) findViewById(R.id.vegtext);
        nonVeg = (RadioButton) findViewById(R.id.nonvegtext);

        spinnervs = (Spinner) findViewById(R.id.vegsize);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptervs = ArrayAdapter.createFromResource(this,
                R.array.Size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptervs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnervs.setAdapter(adaptervs);
        spinnervs.setOnItemSelectedListener(this);

         spinnernvs = (Spinner) findViewById(R.id.nonvegsize);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapternvs = ArrayAdapter.createFromResource(this,
                R.array.Size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapternvs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnernvs.setAdapter(adapternvs);
        spinnernvs.setOnItemSelectedListener(this);



         spinnervt = (Spinner) findViewById(R.id.vegtype);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptervt = ArrayAdapter.createFromResource(this,
                R.array.VegType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptervt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnervt.setAdapter(adaptervt);
        spinnervt.setOnItemSelectedListener(this);


        spinnernvt = (Spinner) findViewById(R.id.nonvegtype);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapternvt = ArrayAdapter.createFromResource(this,
                R.array.NonvegType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapternvt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnernvt.setAdapter(adapternvt);
        spinnernvt.setOnItemSelectedListener(this);


        Spinner spinnervs1 = (Spinner) findViewById(R.id.vegsize1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptervs1 = ArrayAdapter.createFromResource(this,
                R.array.Size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptervs1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnervs1.setAdapter(adaptervs1);
        spinnervs1.setOnItemSelectedListener(this);

        Spinner spinnernvs1 = (Spinner) findViewById(R.id.nonvegsize1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapternvs1 = ArrayAdapter.createFromResource(this,
                R.array.Size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapternvs1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnernvs1.setAdapter(adapternvs1);
        spinnernvs1.setOnItemSelectedListener(this);



        Spinner spinnervt1 = (Spinner) findViewById(R.id.vegtype1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptervt1 = ArrayAdapter.createFromResource(this,
                R.array.VegType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptervt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnervt1.setAdapter(adaptervt1);
        spinnervt1.setOnItemSelectedListener(this);


        Spinner spinnernvt1 = (Spinner) findViewById(R.id.nonvegtype1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapternvt1 = ArrayAdapter.createFromResource(this,
                R.array.NonvegType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapternvt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnernvt1.setAdapter(adapternvt1);
        spinnernvt1.setOnItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if(bundle.getString("source").equals("AvailCoupon"))
            {
                spinnervs.setSelection(1);
                spinnervs.setEnabled(false);
                spinnervs.setClickable(false);

                spinnernvs.setSelection(1);
                spinnernvs.setEnabled(false);
                spinnernvs.setClickable(false);

                size1 = "Regular";
                size = "Regular";

                more.setVisibility(View.GONE);
                add.setText("Add");
                add.setGravity(Gravity.CENTER);
            }
        }



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rc = new Intent(getApplicationContext(), RedeemCoupon.class);
                rc.putExtra("source","frommore");
                startActivity(rc);
            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreitems= (RelativeLayout) findViewById(R.id.pizza);
                moreitems.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // On selecting a spinner item

        switch (adapterView.getId()){
            case R.id.vegtype:
                item = adapterView.getItemAtPosition(i).toString();
                if (!item.equals("Item")) {
                    item1 =item;
                }
                // Showing selected spinner item
                break;
            case R.id.vegsize:
                size = adapterView.getItemAtPosition(i).toString();
                if (!size.equals("Size")) {
                   size1 =size;
                }
                // Showing selected spinner item
                break;

            case R.id.nonvegtype:
                item = adapterView.getItemAtPosition(i).toString();
                if (!item.equals("Item")) {
                    item2 =item;
                }
                // Showing selected spinner item
                break;
            case R.id.nonvegsize:
                size = adapterView.getItemAtPosition(i).toString();
                if (!size.equals("Size")) {
                    size2 =size;

                }
                // Showing selected spinner item
                break;
        }

        if(!item1.equals("a") && !size1.equals("a")){
            items.add(item1);
            pizzaSize.put(item1,size1);
        }

        if(!item2.equals("a") && !size2.equals("a")){
            items.add(item2);
            pizzaSize.put(item2,size2);
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void radioSelected(View v)
    {
        RadioButton radioButton = (RadioButton)v;
        if(((RadioButton) v).getText().equals("Veg"))
        {
            spinnernvs.setVisibility(View.GONE);
            spinnernvt.setVisibility(View.GONE);

            spinnervs.setVisibility(View.VISIBLE);
            spinnervt.setVisibility(View.VISIBLE);

            nonVeg.setChecked(false);
        }

        else if(((RadioButton) v).getText().equals("Non-Veg"))
        {
            spinnervs.setVisibility(View.GONE);
            spinnervt.setVisibility(View.GONE);

            spinnernvs.setVisibility(View.VISIBLE);
            spinnernvt.setVisibility(View.VISIBLE);

            veg.setChecked(false);
        }
    }
}
