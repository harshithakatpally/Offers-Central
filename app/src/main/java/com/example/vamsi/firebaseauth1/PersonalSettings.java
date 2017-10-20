package com.example.vamsi.firebaseauth1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.core.models.UserEntities;

/**
 * Created by FALCONS on 4/25/2017.
 */

public class PersonalSettings extends AppCompatActivity {

    private EditText name;
    private EditText mobileno;
    private EditText email;
    private Switch notificationSwitch;
    private Button apply;
    private User user;
    DatabaseReference rootReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_settings);

        rootReference = FirebaseDatabase.getInstance().getReference();

        name = (EditText) findViewById(R.id.name);
        mobileno = (EditText) findViewById(R.id.mobileno);
        email = (EditText) findViewById(R.id.email);
        notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        apply = (Button) findViewById(R.id.apply);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

//       rootReference.child("Users").child(sharedPreferences.getString("userID","none")).child("name")
//               .addValueEventListener(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(DataSnapshot dataSnapshot) {
//                       name.setText(dataSnapshot.getValue(String.class));
//                   }
//
//                   @Override
//                   public void onCancelled(DatabaseError databaseError) {
//
//                   }
//               });
//
//        rootReference.child("Users").child(sharedPreferences.getString("userID","none")).child("mobile")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        mobileno.setText(dataSnapshot.getValue(String.class));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//        rootReference.child("Users").child(sharedPreferences.getString("userID","none")).child("email")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        email.setText(dataSnapshot.getValue(String.class));
//

        rootReference.child("Users").child(sharedPreferences.getString("userID","none")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                email.setText(user.getEmail());
                mobileno.setText(user.getMobile());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



       apply.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               User tempUser = new User(name.getText().toString(),email.getText().toString(),user.getPassword(),mobileno.getText().toString(),true,true);
               rootReference.child("Users").child(sharedPreferences.getString("userID","none")).setValue(tempUser);
               AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettings.this);
               builder.setTitle("Applied Changes");
               builder.setMessage("Your changes have been saved. Click OK to continue. ");
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent  = new Intent(PersonalSettings.this,HomeScreen.class);
                       startActivity(intent);
                   }
               });
               builder.show();
           }
       });
    }
}
