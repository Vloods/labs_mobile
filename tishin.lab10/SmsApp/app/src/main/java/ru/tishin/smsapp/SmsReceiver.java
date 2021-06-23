package ru.tishin.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        StringBuilder messageText = new StringBuilder();

        if (bundle != null){
            Object[] pduList = (Object[]) bundle.get("pdus");
            SmsMessage[] messageList = new SmsMessage[pduList.length];
            for (int i=0; i<messageList.length; i++){
                messageList[i] = SmsMessage.createFromPdu((byte[]) pduList[i], null);
                messageText.append("Отправитель: ").append(messageList[i].getOriginatingAddress())
                        .append("\n").append("Сообщение: ").append("\n")
                        .append(messageList[i].getMessageBody()).append("\n");
            }
            Toast.makeText(context, messageText.toString(), Toast.LENGTH_LONG).show();
        }

    }
}