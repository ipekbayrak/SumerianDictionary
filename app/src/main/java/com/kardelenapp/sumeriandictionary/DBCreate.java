package com.kardelenapp.sumeriandictionary;

/**
 * Created by mustafa on 11/7/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCreate extends  SQLiteOpenHelper {
    public DBCreate(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME , null, 1);


        this.getReadableDatabase();
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
