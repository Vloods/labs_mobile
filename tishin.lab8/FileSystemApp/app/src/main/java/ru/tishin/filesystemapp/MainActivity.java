package ru.tishin.filesystemapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadFromExternalStorageButtonClick(View view){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,
                    getResources().getString(R.string.text_sandbox_montage_fail),
                    Toast.LENGTH_LONG).show();
            return;
        }

        TextView text_view = (TextView) findViewById(R.id.textView_file);
        FileInputStream istream = null;
        File target_file = new File(Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.file_name));

        if(!target_file.exists()) {
            Toast.makeText(this,
                    getResources().getString(R.string.text_file_not_found),
                    Toast.LENGTH_LONG).show();
            return;
        }

        try {
            try{
                istream = new FileInputStream(target_file);
                byte[] buffer = new byte[istream.available()];
                istream.read(buffer);
                text_view.setText(new String(buffer));
                Toast.makeText(this,
                        getResources().getString(R.string.text_external_storage_load_success),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this,
                        getResources().getString(R.string.text_external_storage_load_fail),
                        Toast.LENGTH_LONG).show();
            }
        } finally {
            if(istream != null) {
                try {
                    istream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadFromSandboxButtonClick(View view) {
        TextView text_view = (TextView) findViewById(R.id.textView_file);

        FileInputStream istream = null;

        try {
            try{
                istream = openFileInput(getResources().getString(R.string.file_name));
                byte[] buffer = new byte[istream.available()];
                istream.read(buffer);
                text_view.setText(new String(buffer));
                Toast.makeText(this,
                        getResources().getString(R.string.text_sandbox_load_success),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this,
                        getResources().getString(R.string.text_sandbox_load_fail),
                        Toast.LENGTH_LONG).show();
            }
        } finally {
            if(istream != null) {
                try {
                    istream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveToExternalStorageButtonClick(View view) {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,
                    getResources().getString(R.string.text_sandbox_montage_fail),
                    Toast.LENGTH_LONG).show();
            return;
        }

        EditText edit_text = (EditText) findViewById(R.id.edittext_file);
        String str_buffer = edit_text.getText().toString();

        FileOutputStream ostream = null;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1);
            Toast.makeText(this,
                    getResources().getString(R.string.text_external_storage_permission),
                    Toast.LENGTH_LONG).show();
            return;
        }
        File target_file = new File(Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.file_name));


        try {
            try{
                ostream = new FileOutputStream(target_file);
                ostream.write(str_buffer.getBytes());
                Toast.makeText(this,
                        getResources().getString(R.string.text_external_storage_save_success),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this,
                        getResources().getString(R.string.text_external_storage_save_fail),
                        Toast.LENGTH_LONG).show();
            }
        } finally {
            if(ostream != null) {
                try {
                    ostream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveToSandboxButtonClick(View view) {
        EditText edit_text = (EditText) findViewById(R.id.edittext_file);
        String str_buffer = edit_text.getText().toString();

        FileOutputStream ostream = null;

        try {
            try{
                ostream = openFileOutput(getResources().getString(R.string.file_name), MODE_PRIVATE);
                ostream.write(str_buffer.getBytes());
                Toast.makeText(this,
                        getResources().getString(R.string.text_sandbox_save_success),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this,
                        getResources().getString(R.string.text_sandbox_save_fail),
                        Toast.LENGTH_LONG).show();
            }
        } finally {
            if(ostream != null) {
                try {
                    ostream.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}