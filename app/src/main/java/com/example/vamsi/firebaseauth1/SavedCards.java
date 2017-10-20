package com.example.vamsi.firebaseauth1;

/**
 * Created by vamsi on 21/5/17.
 */

public class SavedCards {

    public String cardNumber,month,year,cvv;

    public SavedCards()
    {

    }

    public SavedCards(String cardNumber,String month,String year,String cvv)
    {
        this.cardNumber = cardNumber;
        this.month = month;
        this.year = year;
        this.cvv = cvv;
    }

    public String getCardNumber()
    {
        return cardNumber;
    }

    public String getMonth()
    {
        return month;
    }

    public String getYear()
    {
        return year;
    }

    public String getCvv()
    {
        return cvv;
    }
}
