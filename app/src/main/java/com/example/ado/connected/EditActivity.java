package com.example.ado.connected;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ado on 2016-11-03.
 */

public class EditActivity extends FragmentActivity {

    private final String LOG_TAG = EditActivity.class.getSimpleName();
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private Button mButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mNameTextView = (TextView) findViewById(R.id.connectedName);
        mPhoneTextView = (TextView) findViewById(R.id.connectedPhone);
        mEmailTextView = (TextView) findViewById(R.id.connectedEmail);

        mContentResolver = EditActivity.this.getContentResolver();

        Intent intent = new Intent();
        final String _id = intent.getStringExtra(ConnectedContract.Connected.CONNECTED_ID);
        String name = intent.getStringExtra(ConnectedContract.Connected.CONNECTED_NAME);
        String phone = intent.getStringExtra(ConnectedContract.Connected.CONNECTED_PHONE);
        String email = intent.getStringExtra(ConnectedContract.Connected.CONNECTED_EMAIL);

        mNameTextView.setText(name);
        mPhoneTextView.setText(phone);
        mEmailTextView.setText(email);

        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();
                values.put(ConnectedContract.ConnectedColumns.CONNECTED_NAME, mNameTextView.getText().toString());
                values.put(ConnectedContract.ConnectedColumns.CONNECTED_PHONE, mPhoneTextView.getText().toString());
                values.put(ConnectedContract.ConnectedColumns.CONNECTED_EMAIL, mEmailTextView.getText().toString());
                Uri uri = ConnectedContract.Connected.buildConnectedUri(_id);
                int updatedRecord = mContentResolver.update(uri, values, null, null);
                Log.d(LOG_TAG, "Numbers of records updated " + updatedRecord);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}
