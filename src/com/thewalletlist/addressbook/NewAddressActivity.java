package com.thewalletlist.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

public class NewAddressActivity extends Activity {

  @Override public void onCreate(Bundle icy) {
    super.onCreate(icy);

    Intent intent = getIntent();
    String label = intent.getStringExtra(C.EXTRA_LABEL);

    setContentView(R.layout.new_address);

    // if we received a label, it's edit mode. prepopulate as much as possible.
    if (label != null) {
      AddressBook book = AddressBook.load(this);
      Address a = book.getByLabel(label);

      if (a == null) {
        return;
      }

      EditText et_label = (EditText) findViewById(R.id.edittext_label);
      et_label.setText(label);
      EditText et_addy = (EditText) findViewById(R.id.edittext_address);
      et_addy.setText(a.getAddress());
    }
  }

  public void doScanButton(View view) {
    IntentIntegrator integrator = new IntentIntegrator(this);
    integrator.initiateScan();
  }

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (scanResult != null) {
      String res = scanResult.getContents();
      EditText et_addy = (EditText) findViewById(R.id.edittext_address);
      et_addy.setText(res);
    }
  }

  public void doAddButton(View view) {
    EditText et_addy = (EditText) findViewById(R.id.edittext_address);
    String addy = et_addy.getText().toString();
    addy = addy.trim();
    EditText et_label = (EditText) findViewById(R.id.edittext_label);
    String label = et_label.getText().toString();
    label = label.trim();
    if (addy.equals("") || label.equals("")) {
      Toast.makeText(this, "you gotta input SOMETHING", Toast.LENGTH_SHORT).show();
      return;
    }
    Address address = new Address(label, addy);
    AddressBook book = AddressBook.load(this);
    book.add(address);
    book.save(this);

    Intent resultData = new Intent();
    resultData.putExtra(C.EXTRA_LABEL, label);
    setResult(Activity.RESULT_OK, resultData);
    finish();
  }

}
