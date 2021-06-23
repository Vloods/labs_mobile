package ru.tishin.databaseapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.tishin.databaseapp.data.StudentsDbContract.*;

public class StudentsDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "studentsDb.db";
    private final static int DATABASE_VERSION = 1;

    public StudentsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table ").append(Students.TABLE_NAME).append("(")
                .append(Students.COL_ID).append(" integer primary key autoincrement,")
                .append(Students.COL_SURNAME).append(" text(20) not null,")
                .append(Students.COL_NAME).append(" text(20) not null")
                .append(")");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("drop table if exists ").append(Students.TABLE_NAME);
        db.execSQL(sql.toString());
        onCreate(db);
    }
}
