package com.thewalletlist.addressbook;

import java.io.*;
import java.util.ArrayList;
import android.util.Log;
import android.content.Context;
import org.json.*;

public class AddressBook implements Serializable {

  private static String FILENAME = "addressbook";
  private ArrayList<Address> mAddressBook = new ArrayList();

  public AddressBook() {
    return; // results in an empty array
  }

  public AddressBook(JSONArray a) {
    try {
      for (int i = 0; i < a.length(); i++) {
        Address addy = new Address(a.getJSONObject(i));
        mAddressBook.add(addy);
      }
    } catch (JSONException e) {
      Log.e(C.LOG, "AddressBook(): " + e.toString());
    }
  }

  public ArrayList<Address> getAsList() {
    return mAddressBook;
  }

  public int count() {
    return mAddressBook.size();
  }

  public Address getByLabel(String label) {
    for (int i = 0; i < mAddressBook.size(); i++) {
      Address a = mAddressBook.get(i);
      if (a.getLabel().equals(label)) {
        return a;
      }
    }
    return null;
  }

  public int getIndexByLabel(String label) {
    for (int i = 0; i < mAddressBook.size(); i++) {
      Address a = mAddressBook.get(i);
      if (a.getLabel().equals(label)) {
        return i;
      }
    }
    return -1;
  }

  // overwrites if the label matches an existing entry
  public void add(Address address) {
    int idx = getIndexByLabel(address.getLabel());
    if (idx == -1) {
      mAddressBook.add(address);
      return;
    } else {
      mAddressBook.set(idx, address);
    }
  }

  public void delete(Address address) {
    for (int i = 0; i < mAddressBook.size(); i++) {
      Address a = mAddressBook.get(i);
      if (a.getLabel().equals(address.getLabel())) {
        mAddressBook.remove(i);
      }
    }
  }

  public static boolean hasAddressBook(Context c) {
    String[] files = c.fileList();
    for (int i = 0; i < files.length; i++) {
      if (files[i].equals(FILENAME)) {
        return true;
      }
    }
    return false;
  }

  public static AddressBook load(Context c) {
    if (hasAddressBook(c)) {
      Log.d(C.LOG, "looks like we have local data");
      try {
        FileInputStream fis = c.openFileInput(FILENAME);
        String s = Util.convertStreamToString(fis);
        JSONArray a = new JSONArray(s);
        return new AddressBook(a);
      } catch (Exception e) {
        Log.e(C.LOG, "AddressBook.load: " + e.toString());
        // should only happen on upgrades, when types are incompatible.
        return new AddressBook();
      }
    } else {
      Log.d(C.LOG, "empty address book");
      return new AddressBook();
    }
  }

  public void save(Context c) {
    JSONArray a = toJSON();
    String saveme = a.toString();
    FileOutputStream fos = null;
    try {
      fos = c.openFileOutput(FILENAME, Context.MODE_PRIVATE);
      fos.write(saveme.getBytes());
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (fos != null) {
          fos.flush();
          fos.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  public JSONArray toJSON() {
    JSONArray arr = new JSONArray();
    for (int i = 0; i < mAddressBook.size(); i++) {
      Address addy = mAddressBook.get(i);
      arr.put(addy.toJSON());
    }
    return arr;
  }

}
