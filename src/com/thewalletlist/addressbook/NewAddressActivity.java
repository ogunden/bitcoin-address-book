package com.thewalletlist.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;


public class NewAddressActivity extends FragmentActivity {

  String mId;

  String mScanResult;
  String mParsedLabel;

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

  public void onResume() {
    super.onResume();

    if (mScanResult != null) {
      AddressEntry e = Util.parseBitcoinURI(mScanResult);
      String address = e.getAddress();
      EditText et_addy = (EditText) findViewById(R.id.edittext_address);
      et_addy.setText(address);

      mParsedLabel = e.getLabel();
      EditText etLabel = (EditText) findViewById(R.id.edittext_label);
      String currLabel = etLabel.getText().toString();
      if (mParsedLabel != null && currLabel.isEmpty()) {
        etLabel.setText(mParsedLabel);
      } else if (mParsedLabel != null && !mParsedLabel.equals(currLabel)) {
        DialogFragment f = new UseLabelDialogFragment();
        f.show(getSupportFragmentManager(), "label");
      }
    }

    mScanResult = null;
  }

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (scanResult != null) {
      mScanResult = scanResult.getContents();
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

  public class UseLabelDialogFragment extends DialogFragment {

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the Builder class for convenient dialog construction
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      String message = "error - mParsedLabel null";
      if (mParsedLabel != null) {
        message = "use label '" + mParsedLabel + "' from qr code?";
      }
      builder.setMessage(message)
             .setPositiveButton("yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 EditText etLabel = (EditText) findViewById(R.id.edittext_label);
                 etLabel.setText(mParsedLabel);
               }
             })
             .setNegativeButton("no", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                     // User cancelled the dialog
                 }
             });
      // Create the AlertDialog object and return it
      return builder.create();
  }
  }

}
