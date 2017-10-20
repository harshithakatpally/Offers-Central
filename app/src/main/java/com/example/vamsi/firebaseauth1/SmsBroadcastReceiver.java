package com.example.vamsi.firebaseauth1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by vamsi on 4/6/17.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage smsMessage = smsMessages[0];

        //Toast.makeText(context, "SMS Received", Toast.LENGTH_SHORT).show();

        Intent i = new Intent("otp");
        i.putExtra("message",smsMessage.getMessageBody().substring(16,22));
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }
}
