package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by FALCONS on 5/18/2017.
 */

public class SetReminder extends AppCompatActivity {

    boolean oneDay,twoDays,threeDays;
    private Button saveButton;
    private DatabaseReference rootReference;
    private Switch statusSwitch;
    private FirebaseAuth firebaseAuth;
    private EditText timeEditText,descriptionEditText;
    private boolean status;
    private String time,description;
    private LinearLayout daysLayout,timeLayout;
    private TextView dayHeading,mainHeading;
    private CheckBox day1,day2,day3;
    private View line;
    private ReminderSettings reminderSettings;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_reminder);

        daysLayout = (LinearLayout) findViewById(R.id.daysLayout);
        timeLayout = (LinearLayout) findViewById(R.id.timeLayout);

        mainHeading = (TextView) findViewById(R.id.mainHeading);
        dayHeading = (TextView) findViewById(R.id.daysHeading);

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        day1 = (CheckBox) findViewById(R.id.day1);
        day2 = (CheckBox) findViewById(R.id.day2);
        day3 = (CheckBox) findViewById(R.id.day3);

        line = findViewById(R.id.firstLine1);

        timeEditText = (EditText) findViewById(R.id.timeEditText);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"time picker");
            }
        });

        descriptionEditText = (EditText) findViewById(R.id.description);

        statusSwitch = (Switch) findViewById(R.id.status);
        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    status = true;
                    descriptionEditText.setVisibility(View.VISIBLE);
                    dayHeading.setVisibility(View.VISIBLE);
                    daysLayout.setVisibility(View.VISIBLE);
                    timeLayout.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                }
                else {
                    status = false;
                    descriptionEditText.setVisibility(View.INVISIBLE);
                    dayHeading.setVisibility(View.INVISIBLE);
                    daysLayout.setVisibility(View.INVISIBLE);
                    timeLayout.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Checking to see if the reminder is already set and belongs to which type : general or specific
        final Bundle bundle = getIntent().getExtras();
        if(bundle.getString("type").equals("specific"))
        {
            mainHeading.setText("Reminder Settings for : "+bundle.get("couponID"));

        }
        else if(bundle.getString("type").equals("general"))
        {
            mainHeading.setText("General Reminder Settings");
            rootReference.child("Reminders").child(firebaseAuth.getCurrentUser().getUid())
                    .child("GeneralSettings").addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue()!=null) {

                        reminderSettings = dataSnapshot.getValue(ReminderSettings.class);
                        if(reminderSettings.isStatus())
                        {
                            statusSwitch.setChecked(true);

                            //Setting the values fetched from the database
                            descriptionEditText.setText(reminderSettings.getDescription());

                            String days[] = reminderSettings.getDays().split(",");

                            if(days[0].equals("true"))
                                day1.setChecked(true);
                            if(days[1].equals("true"))
                                day2.setChecked(true);
                            if(days[2].equals("true"))
                                day3.setChecked(true);

                            timeEditText.setText(reminderSettings.getTime());

                        }

                        else
                        {
                            statusSwitch.setChecked(false);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String days = "";
                days = oneDay+","+twoDays+","+threeDays;

//                if(status)
//                {
//                    time = timeEditText.getText().toString();
//                    description = descriptionEditText.getText().toString();
//                    Toast.makeText(SetReminder.this,time+days+status, Toast.LENGTH_LONG).show();
//                    ReminderSettings reminderSettings = new ReminderSettings(status,days,time,description);
//                    rootReference.child("Reminders").child(firebaseAuth.getCurrentUser().getUid())
//                            .child("GeneralSettings").setValue(reminderSettings);
//                }

                time = timeEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                ReminderSettings reminderSettings = new ReminderSettings(status,days,time,description);
                rootReference.child("Reminders").child(firebaseAuth.getCurrentUser().getUid())
                        .child("GeneralSettings").setValue(reminderSettings);

            }
        });
    }

    public void onCheckboxClicked(View v) {
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.getText().toString().equals("1 day"))
        {
            if(((CheckBox) v).isChecked())
                oneDay = true;
            else
                oneDay = false;
        }

        else if (checkBox.getText().toString().equals("2 days"))
        {
            if(((CheckBox) v).isChecked())
                twoDays = true;
            else
                twoDays = false;
        }


        else if (checkBox.getText().toString().equals("3 days")){
            if(((CheckBox) v).isChecked())
                threeDays = true;
            else
                threeDays = false;
        }

    }
}
