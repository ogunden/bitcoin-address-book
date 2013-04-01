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

}
