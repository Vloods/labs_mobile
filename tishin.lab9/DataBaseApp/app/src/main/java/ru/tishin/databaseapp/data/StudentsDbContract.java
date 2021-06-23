package ru.tishin.databaseapp.data;

import android.provider.BaseColumns;

public final class StudentsDbContract {
    private StudentsDbContract() {
    }

    public static class Students implements BaseColumns {
        public final static String TABLE_NAME = "students";

        public final static String COL_ID = BaseColumns._ID;
        public final static String COL_SURNAME = "surname";
        public final static String COL_NAME = "name";
    }
}
