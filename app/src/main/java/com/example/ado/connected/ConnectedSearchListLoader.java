package com.example.ado.connected;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ado on 2016-11-03.
 */

public class ConnectedSearchListLoader extends AsyncTaskLoader<List<Connected>> {
    private static final String LOG_TAG = ConnectedSearchListLoader.class.getSimpleName();
    private List<Connected> mConnectedFriendList;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private String mFilterText;


    public ConnectedSearchListLoader(Context context, Uri uri, ContentResolver contentResolver, String filterText) {
        super(context);
        mContentResolver = contentResolver;
        mFilterText = filterText;

    }

    @Override
    public List<Connected> loadInBackground() {
        String [] projection = {BaseColumns._ID,
        ConnectedContract.ConnectedColumns.CONNECTED_NAME,
        ConnectedContract.ConnectedColumns.CONNECTED_PHONE,
        ConnectedContract.ConnectedColumns.CONNECTED_EMAIL};

        List<Connected> entries = new ArrayList<Connected>();

        String selection = ConnectedContract.ConnectedColumns.CONNECTED_NAME + " LIKE '" + mFilterText + "%'";
        mCursor = mContentResolver.query(ConnectedContract.URI_TABLE, projection, selection, null, null);
        if(mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String name = mCursor.getString(mCursor.getColumnIndex(
                            ConnectedContract.ConnectedColumns.CONNECTED_NAME));
                    String phone = mCursor.getString(mCursor.getColumnIndex(
                            ConnectedContract.ConnectedColumns.CONNECTED_PHONE));
                    String email = mCursor.getString(mCursor.getColumnIndex(
                            ConnectedContract.ConnectedColumns.CONNECTED_EMAIL));

                    Connected connectedFriends = new Connected(_id, name, phone, email);
                    entries.add(connectedFriends);
                }while (mCursor.moveToNext());
            }
        }
        return entries;
    }


    @Override
    public void deliverResult(List<Connected> connectedFriends) {
        if(isReset()) {
            if(connectedFriends != null) {
                mCursor.close();
            }
        }

        List<Connected> oldConnectedFriendList = mConnectedFriendList;
        if(mConnectedFriendList == null || mConnectedFriendList.size() == 0) {
            Log.d(LOG_TAG, "+++++++++ NO DATA RETURNED");
        }

        mConnectedFriendList = connectedFriends;
        if(isStarted()) {
            super.deliverResult(connectedFriends);
        }

        if(oldConnectedFriendList != null && oldConnectedFriendList != connectedFriends) {
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mConnectedFriendList != null) {
            deliverResult(mConnectedFriendList);
        }

        if(takeContentChanged() || mConnectedFriendList == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null) {
            mCursor.close();
        }

        mConnectedFriendList = null;
    }

    @Override
    public void onCanceled(List<Connected> connectedFriends) {
        super.onCanceled(connectedFriends);
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
