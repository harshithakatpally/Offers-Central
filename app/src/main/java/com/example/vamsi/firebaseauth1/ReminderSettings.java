package com.example.vamsi.firebaseauth1;

/**
 * Created by vamsi on 23/5/17.
 */

public class ReminderSettings {

    private boolean status;
    private String days,time,description;

    public ReminderSettings()
    {

    }

    public ReminderSettings(boolean status,String days,String time,String description)
    {
        this.status = status;
        this.days = days;
        this.time = time;
        this.description = description;
    }

    public String getDays()
    {
        return days;
    }

    public String getTime()
    {
        return time;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isStatus()
    {
        return status;
    }
}
