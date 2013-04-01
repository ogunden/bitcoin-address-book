package com.thewalletlist.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;

public class AddressDetailActivity extends Activity {

  private AddressEntry mAddress;

  private static final int EDIT_REQUEST_ID = 0;

  @Override public void onCreate(Bundle icy) {
    super.onCreate(icy);

    Intent intent = getIntent();
    String id = intent.getStringExtra(C.EXTRA_ID);
    if (id == null) {
      setResult(Activity.RESULT_CANCELED);
      finish();
    }

    AddressBook addressBook = AddressBook.load(this);
    mAddress = addressBook.getById(id);

    if (mAddress == null) {
      setResult(Activity.RESULT_CANCELED);
      finish();
    }

    setContentView(R.layout.address_detail);


  }

  private void redraw() {
    TextView labelView = (TextView) findViewById(R.id.textview_label);
    labelView.setText(mAddress.getLabel());

    TextView addressView = (TextView) findViewById(R.id.textview_address);
    addressView.setText(mAddress.getAddress());
  }

  @Override public void onResume() {
    super.onResume();
    redraw();
  }

  public void doQRButton(View view) {
    IntentIntegrator integrator = new IntentIntegrator(this);
    integrator.shareText("bitcoin:" + mAddress.getAddress());
  }

  public void doEditButton(View view) {
    Intent intent = new Intent(this, NewAddressActivity.class);
    intent.putExtra(C.EXTRA_ID, mAddress.getId());
    startActivityForResult(intent, EDIT_REQUEST_ID);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == EDIT_REQUEST_ID) {
      if (resultCode == RESULT_OK) {
        AddressBook addressBook = AddressBook.load(this);
        mAddress = addressBook.getById(mAddress.getId());
        redraw();
      }
    }
  }

  public void doDeleteButton(View view) {
    AddressBook addressBook = AddressBook.load(this);
    addressBook.delete(mAddress);
    addressBook.save(this);

    setResult(Activity.RESULT_OK);
    finish();
  }

}
