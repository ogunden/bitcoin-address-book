package com.thewalletlist.addressbook;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.Menu;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ImageView;

import android.util.Log;

public class AddressEntryAdapter extends ArrayAdapter {
  private int mResource;
  private LayoutInflater mInflater;
  private Context mContext;

  public AddressEntryAdapter (Context c, int resourceId) {
    super(c, resourceId);
    mResource = resourceId;
    mInflater = LayoutInflater.from(c);
    mContext = c;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    /* create a new view of my layout and inflate it in the row */
    convertView = (LinearLayout) mInflater.inflate(mResource, null);

    /* Extract the object to show */
    AddressEntry ae = (AddressEntry) getItem(position);

    /* Take the TextView from layout and set the label */
    TextView txtName = (TextView) convertView.findViewById(R.id.TextLabel);
    txtName.setText(ae.getLabel());

    /* Take the ImageView from layout and set the image */
    ImageView iView = (ImageView) convertView.findViewById(R.id.ImageCoin);
    Resources res = mContext.getResources();
    if (ae.getKind() == AddressEntry.KIND_BITCOIN) {
      iView.setImageDrawable(res.getDrawable(R.drawable.bitcoin));
    } else if (ae.getKind() == AddressEntry.KIND_LITECOIN) {
      iView.setImageDrawable(res.getDrawable(R.drawable.litecoin));
    }
    return convertView;
  }
}
