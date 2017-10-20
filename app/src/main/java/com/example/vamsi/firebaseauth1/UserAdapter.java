package com.example.vamsi.firebaseauth1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by VAMSI on 24-02-2017.
 */

public class UserAdapter extends ArrayAdapter {



    public UserAdapter(Context context, int resource,ArrayList<User> users) {
        super(context,resource,users);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater userInflator = LayoutInflater.from(getContext());
        View userView = userInflator.inflate(R.layout.user_list_ui,parent,false);

        User user = (User) getItem(position);

        TextView userName = (TextView) userView.findViewById(R.id.userName);
        ImageView userImage = (ImageView) userView.findViewById(R.id.userImage);

        userName.setText(user.getEmail());
        userImage.setImageResource(R.mipmap.ic_launcher);

        return userView;
    }
}
