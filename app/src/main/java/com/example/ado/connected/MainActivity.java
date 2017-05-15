package com.example.ado.connected;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(android.R.id.content) == null) {
            ConnectedListFragment connectedListFragment = new ConnectedListFragment();
            fragmentManager.beginTransaction().add(android.R.id.content, connectedListFragment).commit();
        }
//        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addIntent);
                break;

            case R.id.deleteDatabase:
                ConnectedDialog dialog = new ConnectedDialog();
                Bundle args = new Bundle();
                args.putString(ConnectedDialog.DIALOG_TYPE, ConnectedDialog.DELETE_RECORD);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "delete-database");
                break;

            case R.id.search:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
