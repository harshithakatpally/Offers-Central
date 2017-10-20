package com.example.vamsi.firebaseauth1;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by vamsi on 23/5/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    EditText timeEditText;
    String hour,minute;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(),this,hour,minute,false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        timeEditText = (EditText) getActivity().findViewById(R.id.timeEditText);
        String time = hourOfDay+":"+minute;
        timeEditText.setText(time);
    }
}
