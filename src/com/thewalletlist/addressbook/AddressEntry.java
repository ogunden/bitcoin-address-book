package com.thewalletlist.addressbook;

import java.io.Serializable;
import org.json.*;
import android.util.Log;


public class AddressEntry implements Serializable {

  private String mId;
  private String mLabel;
  private String mAddress;

  public AddressEntry(String label, String address) {
    mId = Util.randomString(24);
    mLabel = label;
    mAddress = trimPrefix(address);
  }

  public static AddressEntry ofURL(String URL) {
    // try standard URL parser?
    // retur
  }

  public static String trimPrefix(String address) {
    if (address.startsWith("bitcoin:")) {
      return address.substring(7,address.length() - 8);
    } else {
      return address;
    }
  }

  public AddressEntry(String id, String label, String address) {
    mId = id;
    mLabel = label;
    mAddress = trimPrefix(address);
  }

  public AddressEntry(JSONObject j) {
    try {
      mId = j.getString("id");
      mLabel = j.getString("label");
      mAddress = j.getString("address");
    } catch (JSONException e) {
      Log.e(C.LOG, "Address(): " + e.toString());
    }
  }

  public boolean idEquals(String id) {
    return mId.equals(id);
  }

  public String getId() {
    return mId;
  }

  public String getLabel() {
    return mLabel;
  }

  public String getAddress() {
    return mAddress;
  }

  public void setLabel(String label) {
    mLabel = label;
  }

  public void setAddress(String address) {
    mAddress = address;
  }

  public JSONObject toJSON() {
    try {
      JSONObject j = new JSONObject();
      j.put("id",mId);
      j.put("label", mLabel);
      j.put("address", mAddress);
      return j;
    } catch (JSONException e) {
      Log.e(C.LOG, "Address.toJSON: " + e.toString());
      return null;
    }
  }

  // Used by the ListView as the display string
  public String toString() {
    return mLabel;
  }

}
