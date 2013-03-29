package com.thewalletlist.addressbook;

import java.io.Serializable;
import org.json.*;
import android.util.Log;


public class Address implements Serializable {

  private String mLabel;
  private String mAddress;

  public Address(String label, String address) {
    mLabel = label;
    mAddress = address;
  }

  public Address(JSONObject j) {
    try {
      mLabel = j.getString("label");
      mAddress = j.getString("address");
    } catch (JSONException e) {
      Log.e(C.LOG, "Address(): " + e.toString());
    }
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
