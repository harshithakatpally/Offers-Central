package com.example.vamsi.firebaseauth1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by vamsi on 5/6/17.
 */

public class ResetPassword extends AppCompatActivity {

    private EditText newPassword,confirmNewPassword;
    private Button reset;
    private Bundle bundle;
    private String email;
    private DatabaseReference rootReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reset_password);

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        bundle = getIntent().getExtras();
        email = "";
        if(bundle!=null)
        {
            email = bundle.getString("email");
        }
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmNewPassword = (EditText) findViewById(R.id.confirmNewPassword);
        reset = (Button) findViewById(R.id.resetButton);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = newPassword.getText().toString();
                String confirmPassword = confirmNewPassword.getText().toString();

                if(password.equals(""))
                {
                    Toast.makeText(ResetPassword.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                }

                else if(confirmPassword.equals(""))
                {
                    Toast.makeText(ResetPassword.this, "Please re-enter the password", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    if(!password.equals(confirmPassword))
                    {
                        Toast.makeText(ResetPassword.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        if(!email.equals(""))
                        {
                            rootReference.child("Users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                                    {
                                        User user = userSnapshot.getValue(User.class);
                                        if(user.getEmail().equals(email))
                                        {
                                            rootReference.child("Users").child(userSnapshot.getKey())
                                                    .child("password").setValue(password);
                                            firebaseAuth.getCurrentUser().delete();
                                            firebaseAuth.signInWithEmailAndPassword(email,password)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            Intent i = new Intent(ResetPassword.this,HomeScreen.class);
                                                            startActivity(i);
                                                        }
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

            }
        });
    }
}
