package com.profiler.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Profiler";

    public ProfileDatabaseHelper(Context context, int dbVersion) {
        super(context, DATABASE_NAME, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Android requires _id
            String createSql;
            createSql = "CREATE TABLE profile (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "profile_id INTEGER, " + "name TEXT NOT NULL, " + 
            "wallpaper TEXT NOT NULL, " + "volume INTEGER NOT NULL, " + "ringtone TEXT NOT NULL)";
            createTable(db, createSql);

        } catch (Exception e) {
        	Log.e("PROFILER", "Sql creation failed: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ( newVersion > oldVersion ) {
        	
        }
    }

    private void createTable(SQLiteDatabase db, String query) {
        try {
        	Log.i("PROFILER", "Creating db: " + query);
            db.execSQL(query);
        } catch (Exception e) {
        	Log.e("PROFILER", "Sql creation failed: " + e.getMessage());
        }
    }
}