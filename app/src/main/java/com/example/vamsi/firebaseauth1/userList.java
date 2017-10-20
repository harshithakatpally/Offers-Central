package com.example.vamsi.firebaseauth1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by VAMSI on 24-02-2017.
 */
public class userList extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference UserDatabaseRef;
    private ListView user_listview;
    @Override
    protected void onCreate(@Nullable Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.user_listview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        UserDatabaseRef = firebaseDatabase.getReference("Users");
        user_listview= (ListView)findViewById(R.id.userListView);
    }

    protected void onStart()
    {
        super.onStart();

        ValueEventListener userListener = new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> userIDs = new ArrayList<>();
                for(DataSnapshot userID : dataSnapshot.getChildren())
                {
                    User user = userID.getValue(User.class);
                    userIDs.add(user);
                }


                /*Iterator<User> userIDsIterator= userIDs.iterator();
                while(userIDsIterator.hasNext())
                {
                    String email = (userIDsIterator.next()).getEmail();
                    Toast.makeText(MainActivity.this,email,Toast.LENGTH_SHORT).show();
                }*/
                UserAdapter la = new UserAdapter(getApplicationContext(),R.layout.user_list_ui,userIDs);

                user_listview.setAdapter(la);
                user_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        /*if(view instanceof ImageButton)
                        {
                            Toast.makeText(userList.this,"You clicked the image",Toast.LENGTH_SHORT).show();
                        }

                        if(view instanceof TextView)
                        {
                            Toast.makeText(userList.this,"User clicked : "+((TextView) view).getText().toString(),Toast.LENGTH_SHORT).show();
                        }*/

                        Toast.makeText(userList.this,"clicked item position : "+i,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        UserDatabaseRef.addValueEventListener(userListener);
    }
}
