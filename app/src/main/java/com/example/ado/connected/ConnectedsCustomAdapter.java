package com.example.ado.connected;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ado on 2016-11-03.
 */

public class ConnectedsCustomAdapter extends ArrayAdapter<Connected> {

    // in this class we are adapting the main view for our content, and setting up all the links to view components.

    private LayoutInflater mLayoutInflator;
    private static FragmentManager sFragmentManager;

    public ConnectedsCustomAdapter(Context context, FragmentManager fragmentManager) {
        super(context, android.R.layout.simple_list_item_2);
        mLayoutInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null) {
            view = mLayoutInflator.inflate(R.layout.custom_connected, parent, false);
        }else{
            view = convertView;
        }

        final Connected connected = getItem(position);
        final int _id = connected.getId();
        final String name = connected.getName();
        final String phone = connected.getPhone();
        final String email = connected.getEmail();


        ((TextView) view.findViewById(R.id.connected_name)).setText(name);
        ((TextView) view.findViewById(R.id.connected_phone)).setText(phone);
        ((TextView) view.findViewById(R.id.connected_email)).setText(email);


        Button editButton = (Button) view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditActivity.class);

                intent.putExtra(ConnectedContract.ConnectedColumns.CONNECTED_ID, String.valueOf(_id));
                intent.putExtra(ConnectedContract.ConnectedColumns.CONNECTED_NAME, name);
                intent.putExtra(ConnectedContract.ConnectedColumns.CONNECTED_PHONE, phone);
                intent.putExtra(ConnectedContract.ConnectedColumns.CONNECTED_EMAIL, email);
                getContext().startActivity(intent);
            }
        });

        Button deleteButton = (Button) view.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectedDialog dialog = new ConnectedDialog();
                Bundle args = new Bundle();

                args.putString(ConnectedDialog.DIALOG_TYPE, ConnectedDialog.DELETE_RECORD);
                args.putInt(ConnectedContract.ConnectedColumns.CONNECTED_ID, _id);
                args.putString(ConnectedContract.ConnectedColumns.CONNECTED_NAME, name);
                dialog.setArguments(args);
                dialog.show(sFragmentManager, "delete-record");
            }
        });

        return view;
    }

    public void setData(List<Connected> connectedFriends) {
        clear();
        if(connectedFriends != null) {
            for (Connected connected : connectedFriends) {
                add(connected);
            }
        }
    }
}
