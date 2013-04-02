package com.thewalletlist.addressbook;

import java.io.Serializable;
import org.json.*;
import android.util.Log;


public class AddressEntry implements Serializable {

  public static final int KIND_BITCOIN = 0;
  public static final int KIND_LITECOIN = 1;

  private String mId;
  private String mLabel;
  private String mAddress;

  private int mKind;

  public AddressEntry(String label, String address) {
    mId = Util.randomString(24);
    mLabel = label;
    mAddress = address;
    mKind = determineKind(address);
  }

  public static int determineKind(String address) {
    if (address.startsWith("L")) {
      return KIND_LITECOIN;
    } else {
      return KIND_BITCOIN;
    }
  }

  public AddressEntry(String id, String label, String address) {
    mId = id;
    mLabel = label;
    mAddress = address;
    mKind = determineKind(address);
  }

  public AddressEntry(JSONObject j) {
    try {
      mId = j.getString("id");
      mLabel = j.getString("label");
      mAddress = j.getString("address");
      mKind = j.getInt("kind");
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

  public int getKind() {
    return mKind;
  }

  public void setLabel(String label) {
    mLabel = label;
  }

  public void setAddress(String address) {
    mAddress = address;
    mKind = determineKind(address);
  }

  public JSONObject toJSON() {
    try {
      JSONObject j = new JSONObject();
      j.put("id",mId);
      j.put("label", mLabel);
      j.put("address", mAddress);
      j.put("kind", mKind);
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
