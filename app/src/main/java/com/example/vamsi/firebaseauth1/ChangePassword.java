package com.example.vamsi.firebaseauth1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by VAMSI on 07-03-2017.
 */

public class ChangePassword extends AppCompatActivity {

    private Button changePassword;

    @Override
    protected void onCreate(@Nullable Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.change_password);

        changePassword = (Button) findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangePassword.this,"Reached End",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
