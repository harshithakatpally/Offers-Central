package com.example.vamsi.firebaseauth1;

/**
 * Created by vamsi on 21/5/17.
 */

public class WalletDetails {

    public String balance;
    public String lastUpdated;

    public WalletDetails()
    {

    }

    public WalletDetails(String balance,String lastUpdated)
    {
        this.balance = balance;
        this.lastUpdated = lastUpdated;
    }

    public String getBalance()
    {
        return balance;
    }

    public String getLastUpdated()
    {
        return lastUpdated;
    }
}
