package ru.tishin.databaseapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ru.tishin.databaseapp.data.StudentsDbContract.*;
import ru.tishin.databaseapp.data.StudentsDbHelper;

public class MainActivity extends AppCompatActivity {

    private StudentsDbHelper dbHelper = null;

    private EditText edit_text_name = null;
    private EditText edit_text_surname = null;

    private String surname = new String();
    private String name = new String();
    private int entry_id = 0;

    private Button button_edit = null;
    private Button button_delete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new StudentsDbHelper(this);
        edit_text_surname = (EditText) findViewById(R.id.edit_text_surname);
        edit_text_name = (EditText) findViewById(R.id.edit_text_name);

        button_edit = (Button) findViewById(R.id.button_edit);
        button_delete = (Button) findViewById(R.id.button_delete);

        button_edit.setEnabled(false);
        button_delete.setEnabled(false);

        ListView listView = (ListView) findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ArrayList<String> entry = (ArrayList<String>) parent.getItemAtPosition(position);

                surname = entry.get(0);
                edit_text_surname.setText(surname);

                name = entry.get(1);
                edit_text_name.setText(name);

                entry_id = Integer.parseInt(entry.get(2));

                button_edit.setEnabled(true);
                button_delete.setEnabled(true);

            }
        });
    }

    public void showButtonClick(View view) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from " + Students.TABLE_NAME, null);
        try {
            int idx = cursor.getColumnIndex(Students.COL_ID);
            int surIdx = cursor.getColumnIndex(Students.COL_SURNAME);
            int namIdx = cursor.getColumnIndex(Students.COL_NAME);
            while (cursor.moveToNext()) {
                ArrayList<String> entry = new ArrayList<>();
                entry.add(cursor.getString(surIdx));
                entry.add(cursor.getString(namIdx));
                entry.add(cursor.getString(idx));

                arrayList.add(entry);
            }
        } finally {
            cursor.close();
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        ListAdapter customAdapter = new ListAdapter(this, R.layout.list_view, arrayList);

        listView.setAdapter(customAdapter);

        button_edit.setEnabled(false);
        button_delete.setEnabled(false);
    }

    public void addButtonClick(View view) {
        StringBuilder sql = new StringBuilder();

        String surname = edit_text_surname.getText().toString();
        String name = edit_text_name.getText().toString();

        sql.append("insert into ").append(Students.TABLE_NAME).append("(")
                .append(Students.COL_SURNAME).append(", ")
                .append(Students.COL_NAME)
                .append(") values(")
                .append("'").append(surname).append("'")
                .append(", ")
                .append("'").append(name).append("'")
                .append(")");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql.toString());

        if (isInserted(surname, name)){
            Toast.makeText(this, "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Произошла ошибка при добавлении записи", Toast.LENGTH_SHORT).show();
        }

        button_edit.setEnabled(false);
        button_delete.setEnabled(false);
    }

    private boolean isInserted(String surname, String name){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        boolean entryInserted = false;

        Cursor cursor = db.rawQuery("select * from " + Students.TABLE_NAME, null);
        try {
            int surIdx = cursor.getColumnIndex(Students.COL_SURNAME);
            int namIdx = cursor.getColumnIndex(Students.COL_NAME);
            while (cursor.moveToNext()) {
                if (cursor.getString(surIdx).equals(surname) &&
                        cursor.getString(namIdx).equals(name)){
                    entryInserted = true;
                }
            }
        } finally {
            cursor.close();
        }

        return entryInserted;
    }

    public void editButtonClick(View view){
        /*
        UPDATE table_name
        SET column1 = value1, column2 = value2, ...
        WHERE condition
         */

        String oldSurname = surname;
        String oldName = name;

        String newSurname = edit_text_surname.getText().toString();
        String newName = edit_text_name.getText().toString();

        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(Students.TABLE_NAME).append(" set ")
                .append(Students.COL_SURNAME).append(" = ").append("'")
                .append(newSurname).append("'")
                .append(", ")
                .append(Students.COL_NAME).append(" = ").append("'")
                .append(newName).append("'")
                .append(" where ").append(Students.COL_ID).append(" = ")
                .append(entry_id);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql.toString());

        if (isDeleted(oldSurname, oldName) && isInserted(newSurname, newName)){
            Toast.makeText(this, "Запись успешно отредактирована", Toast.LENGTH_SHORT).show();
            surname = newSurname;
            name = newName;
        }
        else{
            Toast.makeText(this, "Произошла ошибка при редактировании записи", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteButtonClick(View view){
        /*
        DELETE FROM table_name WHERE condition
         */
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(Students.TABLE_NAME)
                .append(" where ").append(Students.COL_ID).append(" = ")
                .append(entry_id);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql.toString());


        if (isDeleted(surname, name)){
            Toast.makeText(this, "Запись успешно удалена", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Произошла ошибка при удалении записи", Toast.LENGTH_SHORT).show();
        }

        button_edit.setEnabled(false);
        button_delete.setEnabled(false);
    }

    public boolean isDeleted(String surname, String name){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        boolean entryDeleted = true;
        Cursor cursor = db.rawQuery("select * from " + Students.TABLE_NAME, null);
        try {
            int surIdx = cursor.getColumnIndex(Students.COL_SURNAME);
            int namIdx = cursor.getColumnIndex(Students.COL_NAME);
            while (cursor.moveToNext()) {
                if (cursor.getString(surIdx).equals(surname) &&
                        cursor.getString(namIdx).equals(name)){
                    entryDeleted = false;
                }
            }
        } finally {
            cursor.close();
        }

        return entryDeleted;
    }

}