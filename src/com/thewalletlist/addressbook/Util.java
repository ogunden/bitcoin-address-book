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

}
