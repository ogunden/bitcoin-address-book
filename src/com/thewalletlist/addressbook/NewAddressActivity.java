package com.thewalletlist.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

public class NewAddressActivity extends Activity {

  String mId;

  @Override public void onCreate(Bundle icy) {
    super.onCreate(icy);

    Intent intent = getIntent();
    mId = intent.getStringExtra(C.EXTRA_ID);

    setContentView(R.layout.new_address);

    // if we received an id, it's edit mode. prepopulate as much as possible.
    if (mId != null) {
      AddressBook book = AddressBook.load(this);
      AddressEntry a = book.getById(mId);

      if (a == null) {
        return;
      }

      EditText et_label = (EditText) findViewById(R.id.edittext_label);
      et_label.setText(a.getLabel());
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
    AddressEntry address = null;
    if (mId == null) { // new mode, generate new ID
      address = new AddressEntry(label, addy);
    } else { // edit mode, reuse ID
      address = new AddressEntry(mId, label, addy);
    }
    AddressBook book = AddressBook.load(this);
    book.addOrUpdate(address);
    book.save(this);

    setResult(Activity.RESULT_OK);
    finish();
  }

}
