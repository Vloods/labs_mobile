package ru.tishin.monitoringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private int fileIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startServiceButtonClick(View view) {
        startService(new Intent(this, FileMonitoringService.class));
    }

    public void stopServiceButtonClick(View view) {
        stopService(new Intent(this, FileMonitoringService.class));
    }

    public void addFileButtonClick(View view) {
        try {
            FileOutputStream fos = openFileOutput(getString(R.string.file_name_template, fileIndex++), MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write("Это пример создания файла");
            writer.close();
            fos.close();
            Toast.makeText(this, "Файл успешно был создан!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка при добавлении файла: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}