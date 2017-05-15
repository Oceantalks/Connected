package com.example.ado.connected;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Ado on 2016-11-02.
 */

public class ConnectedDatabase extends SQLiteOpenHelper {


    // This class is a dataBase setup, with the different fields as the id, name, email, and phone.

    private static final String TAG = ConnectedDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "connected.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables {
        String Connected = "connected";
    }

    public ConnectedDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.Connected + "("
        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + ConnectedContract.ConnectedColumns.CONNECTED_NAME + " TEXT NOT NULL,"
        + ConnectedContract.ConnectedColumns.CONNECTED_EMAIL + " TEXT NOT NULL,"
        + ConnectedContract.ConnectedColumns.CONNECTED_PHONE + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        int version = i;
        if(version == 1){
            // add the extra fields without deleting existing data
            version = i1;
        }

        if(version != DATABASE_VERSION){
            db.execSQL("DROP TABLE IF EXISTS " + Tables.Connected);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
