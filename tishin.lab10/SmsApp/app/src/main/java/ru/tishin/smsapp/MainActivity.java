package ru.tishin.smsapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestSmsPermission();
    }

    private void requestSmsPermission() {
        String permission = Manifest.permission.SEND_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void SendSMSClick(View view) {
        EditText phone = (EditText) findViewById(R.id.edit_text_phone);
        EditText message = (EditText) findViewById(R.id.edit_text_message);
        sendSMS(phone.getText().toString(), message.getText().toString());
    }

    private void sendSMS(String phone, String message){
        if (phone.length() == 0 || message.length() == 0){
            Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show();
            return;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage(phone, null, message, pendingIntent, null);
            Toast.makeText(this, "Сообщение было успешно отправлено!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, String.format("Сообщение не удалось отправить. Ошибка: %1$s", e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }
}