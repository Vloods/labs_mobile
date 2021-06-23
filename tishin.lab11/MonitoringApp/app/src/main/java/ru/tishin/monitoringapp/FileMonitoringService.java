package ru.tishin.monitoringapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

public class FileMonitoringService extends Service {
    private Hashtable<String, String> fileListHash = null;
    private boolean isCanceled = false;
    private Thread monitoringThread = null;
    private Context thisContext = null;
    private ArrayList<String> resultList = new ArrayList<>();

    public FileMonitoringService() {
        thisContext = this;
        fileListHash = new Hashtable<>();
        monitoringThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (!isCanceled){
                            String[] files = fileList();
                            for (String file: files) {
                                if (!fileListHash.containsKey(file)) {
                                    fileListHash.put(file, file);
                                    resultList.add(file);

                                }
                            }
                        }
                    }
                }
        );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initFileList();
        Toast.makeText(this, "Сервис для монииторинга файлов успешно создан!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        isCanceled = true;
        Toast.makeText(this, "Сервис для монииторинга файлов благополучно сдох!", Toast.LENGTH_LONG).show();
        Toast.makeText(this, String.format("Было найдено %1$d новых файлов", resultList.size()), Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isCanceled = false;
        if (monitoringThread != null)
            monitoringThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void initFileList(){
        String[] files = fileList();
        for (String file: files)
            fileListHash.put(file, file);
    }
}