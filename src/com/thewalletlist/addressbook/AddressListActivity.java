package com.thewalletlist.addressbook;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;

import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.Menu;

import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;

import android.view.ActionMode;

import android.util.Log;

public class AddressListActivity extends ListActivity {

  ArrayAdapter mAdapter;
  ActionMode mActionMode; // state for action bar menu

  /** Called when the activity is first created. */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: eventually make custom adapter with simple_list_item_2
    // http://stackoverflow.com/questions/11722885/what-is-difference-between-android-r-layout-simple-list-item-1-and-android-r-lay
    mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
    ListView listView = getListView();
    listView.setAdapter(mAdapter);
    listView.setClickable(true);

    OnItemClickListener listListener =
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View view, int pos, long whatisit) {
          AddressEntry addr =
            (AddressEntry) getListView().getItemAtPosition(pos);
          Intent intent = new Intent(AddressListActivity.this, AddressDetailActivity.class);
          intent.putExtra(C.EXTRA_ID, addr.getId());
          startActivity(intent);
        }
      };
    listView.setOnItemClickListener(listListener);
  }

  @Override protected void onResume() {
    super.onResume();

    AddressBook b = AddressBook.load(this);
    Log.d(C.LOG, "got book with " + b.count() + " entries");
    redraw(b);
  }

  private void redraw(AddressBook b) {
    ArrayList<AddressEntry> addresses = b.getAsList();
    mAdapter.clear();
    for (int i = 0; i < addresses.size(); i++) {
      mAdapter.add(addresses.get(i));
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_plus:
        Intent i = new Intent(this, NewAddressActivity.class);
        startActivity(i);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
