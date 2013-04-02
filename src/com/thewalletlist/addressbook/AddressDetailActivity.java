package com.thewalletlist.addressbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.util.Log;

import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class AddressDetailActivity extends FragmentActivity {

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
    Log.d(C.LOG, "label is " + mAddress.getLabel());

    ImageView imageCoin = (ImageView) findViewById(R.id.ImageCoin);
    if (mAddress.getKind() == AddressEntry.KIND_BITCOIN) {
      imageCoin.setImageDrawable(
          getResources().getDrawable(R.drawable.bitcoin));
    } else if (mAddress.getKind() == AddressEntry.KIND_LITECOIN) {
      imageCoin.setImageDrawable(
          getResources().getDrawable(R.drawable.litecoin));
    }
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
    String prefix = "";
    int kind = mAddress.getKind();
    if (kind == AddressEntry.KIND_BITCOIN) {
      prefix = "bitcoin:";
    } else if (kind == AddressEntry.KIND_LITECOIN) {
      prefix = "litecoin:";
    }
    integrator.shareText(prefix + mAddress.getAddress());
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
    DialogFragment f = new AreYouSureDialogFragment();
    f.show(getSupportFragmentManager(), "areyousure");
  }

  public void reallyDelete() {
    AddressBook addressBook = AddressBook.load(this);
    addressBook.delete(mAddress);
    addressBook.save(this);

    setResult(Activity.RESULT_OK);
    finish();
  }

  public class AreYouSureDialogFragment extends DialogFragment {

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the Builder class for convenient dialog construction
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      String message = "error - mParsedLabel null";
      if (mAddress != null) {
        message = "really delete address '" + mAddress.getLabel() + "'?";
      }
      builder.setMessage(message)
             .setPositiveButton("yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 reallyDelete();
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
