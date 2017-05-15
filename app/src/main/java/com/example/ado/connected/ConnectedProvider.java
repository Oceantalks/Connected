package com.example.ado.connected;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Ado on 2016-11-02.
 */

public class ConnectedProvider extends ContentProvider {

    // in this class we have finished the different commands to manipulate the data in our dataBase with methods that take in parameters.

    private ConnectedDatabase mOpenHelper;
    private static String TAG = ConnectedProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int CONNECTED = 100;
    private static final int CONNECTED_ID = 101;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority= ConnectedContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "connected", CONNECTED);
        matcher.addURI(authority, "connected/*", CONNECTED_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ConnectedDatabase(getContext());
        return true;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        ConnectedDatabase.deleteDatabase(getContext());
        mOpenHelper = new ConnectedDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONNECTED:
                return ConnectedContract.Connected.CONTENT_TYPE;
            case CONNECTED_ID:
                return ConnectedContract.Connected.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ConnectedDatabase.Tables.Connected);

        switch (match) {
            case CONNECTED:
                break;
            case CONNECTED_ID:
                String id = ConnectedContract.Connected.getConnectedId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.v (TAG, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CONNECTED:
                long recordId = db.insertOrThrow(ConnectedDatabase.Tables.Connected, null, values);
                return ConnectedContract.Connected.buildConnectedUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        Log.v (TAG, "update(uri=" + uri + ", values=" + contentValues.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;
        switch (match) {
            case CONNECTED:
                // Do nothing
                break;
            case CONNECTED_ID:
                String id = ConnectedContract.Connected.getConnectedId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;


            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(ConnectedDatabase.Tables.Connected, contentValues, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(ConnectedContract.URI_TABLE)) {
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String selectionCriteria = selection;

        switch (match) {
            case CONNECTED:
                // Do nothing
                break;
            case CONNECTED_ID:
                String id = ConnectedContract.Connected.getConnectedId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.delete(ConnectedDatabase.Tables.Connected, selectionCriteria, selectionArgs);
    }
}
