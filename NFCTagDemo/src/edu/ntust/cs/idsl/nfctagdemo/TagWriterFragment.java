package edu.ntust.cs.idsl.nfctagdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 * 
 */
public class TagWriterFragment extends Fragment {

    private static final int REQUEST_PICK_CONTACTS = 1;
    private EditText editTextTel;
    private Button buttonPickContacts;
    private Button buttonScan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_writer, container, false);

        editTextTel = (EditText) rootView.findViewById(R.id.editTextTel);
        buttonPickContacts = (Button) rootView.findViewById(R.id.buttonPickContacts);
        buttonPickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_CONTACTS);
            }
        });
        buttonScan = (Button) rootView.findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(TagWriterActivity.getIntent(getActivity(), editTextTel.getText().toString()), 0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_PICK_CONTACTS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Uri uri = data.getData();
                Cursor contactCursor = getActivity().getContentResolver()
                        .query(uri, new String[] { ContactsContract.Contacts._ID }, null, null, null);
                String id = null;
                if (contactCursor.moveToFirst()) {
                    id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                }
                contactCursor.close();
                String phoneNumber = null;
                Cursor phoneCursor = getActivity()
                        .getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ", new String[] { id }, null);
                if (phoneCursor.moveToFirst()) {
                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phoneCursor.close();
                editTextTel.setText(phoneNumber);
            }
        }
    }

}
