package com.example.ado.connected;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by Ado on 2016-11-03.
 */

public class ConnectedListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Connected>> {

    // in this class we compose the ConnectedsCustomAdapter and ConnectedListLoader to connect to a ListFragment Layout.

    private static final String LOG_TAG = ConnectedListFragment.class.getSimpleName();
    private ConnectedsCustomAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Connected> mConnected;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mContentResolver = getActivity().getContentResolver();
        mAdapter = new ConnectedsCustomAdapter(getActivity(), getChildFragmentManager());
        setEmptyText("No friends");
        setListAdapter(mAdapter);
        setListShown(false);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Connected>> onCreateLoader(int id, Bundle args) {
        mContentResolver = getActivity().getContentResolver();
        return new ConnectedListLoader(getActivity(), ConnectedContract.URI_TABLE, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Connected>> loader, List<Connected> connectedFriendsList) {
        mAdapter.setData(connectedFriendsList);
        mConnected = connectedFriendsList;
        if(isResumed()) {
            setListShown(true);
        }else{
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Connected>> loader) {
        mAdapter.setData(null);
    }
}
