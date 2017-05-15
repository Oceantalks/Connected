package com.example.ado.connected;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Ado on 2016-11-03.
 */

public class SearchActivity extends FragmentActivity
        implements LoaderManager.LoaderCallbacks<List<Connected>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private ConnectedsCustomAdapter mConnectedsCustomAdapter;
    private static int LOADER_ID = 2;
    private ContentResolver mContentResolver;
    private List<Connected> connectedFriendsRetrieved;
    private ListView listView;
    private EditText editText;
    private Button mSearchFriendButton;
    private String matchText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        listView = (ListView) findViewById(R.id.searchResultList);
        mSearchFriendButton = (Button) findViewById(R.id.searchButton);
        editText = (EditText) findViewById(R.id.searchName);
        mContentResolver = getContentResolver();
        mConnectedsCustomAdapter = new ConnectedsCustomAdapter(SearchActivity.this, getSupportFragmentManager());
        listView.setAdapter(mConnectedsCustomAdapter);

        mSearchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchText = editText.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
                
            }
        });

    }

    @Override
    public Loader<List<Connected>> onCreateLoader(int id, Bundle args) {
        return new ConnectedSearchListLoader(SearchActivity.this, ConnectedContract.URI_TABLE, this.mContentResolver, matchText);
    }

    @Override
    public void onLoadFinished(Loader<List<Connected>> loader, List<Connected> connectedFriends) {
        mConnectedsCustomAdapter.setData(connectedFriends);
        this.connectedFriendsRetrieved = connectedFriends;
    }

    @Override
    public void onLoaderReset(Loader<List<Connected>> loader) {
        mConnectedsCustomAdapter.setData(null);
    }
}
