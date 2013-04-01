package com.thewalletlist.addressbook;

import java.io.*;
import java.util.Random;

public class Util {

  public static String convertStreamToString(InputStream is)
      throws java.io.IOException {
    BufferedReader reader =
      new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;

    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }

    is.close();

    return sb.toString();
  }

  static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  static Random rnd = new Random();

  public static String randomString(int length) {
    int ablen = AB.length();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(AB.charAt(rnd.nextInt(ablen)));
    }
    return sb.toString();
  }

  public static AddressEntry parseBitcoinURI(String uri) {
    if (uri.startsWith("bitcoin:") || uri.startsWith("litecoin:") {
      String rest = uri.substring(8);
      int idx = rest.indexOf('?');
      if (idx == -1) {
        return new AddressEntry(null, rest);
      }
      String query = rest.substring(idx + 1);
      String[] queries = query.split("&");
      String label = null;
      for (int i = 0; i < queries.length; i++) {
        if (queries[i].startsWith("label=")) {
          label = android.net.Uri.decode(queries[i].substring(6));
        }
      }
      return new AddressEntry(label, rest.substring(0, idx));
    } else {
      return new AddressEntry(null, uri);
    }
  }

}
