package com.example.ado.connected;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ado on 2016-11-02.
 */

public class ConnectedContract {

    // This class contains of mostly hardcoded paths infrastructure that does not change, and pointers/cursors that will be build upon.
    // and are assigned to simplified variables so you do not need to wright miles long paths over nad over again

    interface ConnectedColumns {
        String CONNECTED_ID = "_id";
        String CONNECTED_NAME = "connected_name";
        String CONNECTED_EMAIL = "connected_email";
        String CONNECTED_PHONE = "connected_phone";
    }

    public static final String CONTENT_AUTHORITY = "com.example.ado.connected.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_CONNECTED = "connected";
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_CONNECTED);

    public static final String [] TOP_LEVELS_PATHS = {
            PATH_CONNECTED
    };

    public static class Connected implements ConnectedColumns, BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_CONNECTED).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".connected";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".connected";

        public static Uri buildConnectedUri (String connectedId){
            return CONTENT_URI.buildUpon().appendEncodedPath(connectedId).build();
        }

        public static String getConnectedId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}
