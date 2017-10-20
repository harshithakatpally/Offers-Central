package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by FALCONS on 5/23/2017.
 */

public class PreferencesScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private RadioGroup pref;
    private RadioButton fpref;
    private RadioButton spref;
    private RadioButton mpref;
    private RadioButton selectedRadioButton;
    private LinearLayout foodpref;
    private LinearLayout shoppingpref;
    private   LinearLayout moviespref;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootReference;
    private String foodp1,foodp2,foodp3,shoppingp1,shoppingp2,shoppingp3;
    private Button prefButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_screen);

        prefButton = (Button) findViewById(R.id.prefButton);

        sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Spinner spinnerf1 = (Spinner) findViewById(R.id.foodp1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterf1 = ArrayAdapter.createFromResource(this,
                R.array.Food, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterf1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerf1.setAdapter(adapterf1);
        spinnerf1.setOnItemSelectedListener(this);

        Spinner spinnerf2 = (Spinner) findViewById(R.id.foodp2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterf2 = ArrayAdapter.createFromResource(this,
                R.array.Food, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterf2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerf2.setAdapter(adapterf2);
        spinnerf2.setOnItemSelectedListener(this);

        Spinner spinnerf3 = (Spinner) findViewById(R.id.foodp3);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterf3 = ArrayAdapter.createFromResource(this,
                R.array.Food, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterf3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerf3.setAdapter(adapterf3);
        spinnerf3.setOnItemSelectedListener(this);

        Spinner spinners1 = (Spinner) findViewById(R.id.shoppingp1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters1 = ArrayAdapter.createFromResource(this,
                R.array.Shopping, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapters1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinners1.setAdapter(adapters1);
        spinners1.setOnItemSelectedListener(this);

        Spinner spinners2 = (Spinner) findViewById(R.id.shoppingp2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters2 = ArrayAdapter.createFromResource(this,
                R.array.Shopping, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapters2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinners2.setAdapter(adapters2);
        spinners2.setOnItemSelectedListener(this);

        Spinner spinners3 = (Spinner) findViewById(R.id.shoppingp3);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters3 = ArrayAdapter.createFromResource(this,
                R.array.Shopping, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapters3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinners3.setAdapter(adapters3);
        spinners3.setOnItemSelectedListener(this);

        Spinner spinnerm1 = (Spinner) findViewById(R.id.moviesp1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterm1 = ArrayAdapter.createFromResource(this,
                R.array.Movies, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterm1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerm1.setAdapter(adapterm1);
        spinnerm1.setOnItemSelectedListener(this);

        Spinner spinnerm2 = (Spinner) findViewById(R.id.moviesp2);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterm2 = ArrayAdapter.createFromResource(this,
                R.array.Movies, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterm2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerm2.setAdapter(adapterm2);
        spinnerm2.setOnItemSelectedListener(this);

        Spinner spinnerm3 = (Spinner) findViewById(R.id.moviesp3);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterm3 = ArrayAdapter.createFromResource(this,
                R.array.Movies, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterm3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerm3.setAdapter(adapterm3);
        spinnerm3.setOnItemSelectedListener(this);


        foodpref = (LinearLayout) findViewById(R.id.foodpreferences);
        shoppingpref = (LinearLayout) findViewById(R.id.shoppingpreferences);
        moviespref = (LinearLayout) findViewById(R.id.moviespreferences);
        fpref = (RadioButton) findViewById(R.id.food);
        spref= (RadioButton) findViewById(R.id.shopping);
        mpref= (RadioButton) findViewById(R.id.movies);

        foodpref.setVisibility(View.GONE);
        shoppingpref.setVisibility(View.GONE);
        moviespref.setVisibility(View.GONE);

        pref= (RadioGroup) findViewById(R.id.preferences);
        pref.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                if(pref.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getApplicationContext(), "Select a category", Toast.LENGTH_SHORT).show();
                }
                else {
                    int selectedId = pref.getCheckedRadioButtonId();
// find the radiobutton by returned id
                    selectedRadioButton = (RadioButton) findViewById(selectedId);
                    Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString() + " is selected", Toast.LENGTH_SHORT).show();

                    if (selectedRadioButton == fpref) {
                        foodpref.setVisibility(View.VISIBLE);
                        shoppingpref.setVisibility(View.GONE);
                        moviespref.setVisibility(View.GONE);
                    } else if (selectedRadioButton == spref) {
                        foodpref.setVisibility(View.GONE);
                        shoppingpref.setVisibility(View.VISIBLE);
                        moviespref.setVisibility(View.GONE);

                    } else if (selectedRadioButton == mpref) {
                        foodpref.setVisibility(View.GONE);
                        shoppingpref.setVisibility(View.GONE);
                        moviespref.setVisibility(View.VISIBLE);
                    }
                }

            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();

        //TODO Get the preferences and show brands in that order
        // Showing selected spinner item
        //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        Spinner spinner = (Spinner)adapterView;
        if(!item.equals("None"))
        {
            if(spinner.getId() == R.id.foodp1)
            {
                foodp1 = item;
            }

            if(spinner.getId() == R.id.foodp2)
            {
                foodp2 = item;
            }

            if(spinner.getId() == R.id.foodp3)
            {
                foodp3 = item;
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void prefButtonClicked(View v)
    {

        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child("Brands").child("Food")
                .child(foodp1).child("prefCount").setValue(1);
        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child("Brands").child("Food")
                .child(foodp2).child("prefCount").setValue(2);
        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child("Brands").child("Food")
                .child(foodp3).child("prefCount").setValue(3);

        Intent i = new Intent(this,HomeScreen.class);
        startActivity(i);
    }
}
