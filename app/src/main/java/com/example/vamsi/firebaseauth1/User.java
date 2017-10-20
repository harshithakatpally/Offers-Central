package com.example.vamsi.firebaseauth1;

import java.util.HashMap;

/**
 * Created by VAMSI on 24-02-2017.
 */

public class User {

    public String password,email,name,mobile;
    public boolean emailVerified,mobileVerified;
    //Public constructor for sending type of object as parameter
    public User()
    {
    }

    //Another constructor for initializing the private variables.
    public User(String name,String email,String password,String mobile,boolean emailVerified,boolean mobileVerified)
    {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        this.mobileVerified = mobileVerified;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword()
    {
        return password;
    }
    public  String getName(){
        return name;
    }
    public String getMobile(){return mobile;}

    public boolean isEmailVerified(){return emailVerified;}
    public boolean isMobileVerified(){return mobileVerified;}

}
