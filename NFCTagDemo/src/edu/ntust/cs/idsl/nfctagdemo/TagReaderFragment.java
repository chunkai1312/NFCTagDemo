package edu.ntust.cs.idsl.nfctagdemo;

import org.ndeftools.Message;
import org.ndeftools.wellknown.UriRecord;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 * 
 */
public class TagReaderFragment extends Fragment {

    private static final String EXTRA_MESSAGE = "edu.ntust.cs.idsl.nfctagdemo.extra.MESSAGE";
    private static final String EXTRA_TYPE = "edu.ntust.cs.idsl.nfctagdemo.extra.TAG_TYPE";
    private static final String EXTRA_SIZE = "edu.ntust.cs.idsl.nfctagdemo.extra.MAX_SIZE";
    private TextView textViewTagType;
    private TextView textViewMaxSize;
    private ListView listViewContent;
    private Button buttonScan;

    public static Intent getIntentForResult(String type, int size, Message message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_SIZE, size);
        intent.putExtra(EXTRA_MESSAGE, message.getNdefMessage());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_reader, container, false);

        textViewTagType = (TextView) rootView.findViewById(R.id.textViewTagType);
        textViewMaxSize = (TextView) rootView.findViewById(R.id.textViewMaxSize);
        listViewContent = (ListView) rootView.findViewById(R.id.listViewContent);
        buttonScan = (Button) rootView.findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(TagReaderActivity.getIntent(getActivity()), 0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK == resultCode) {
            NdefMessage ndefMessage = (NdefMessage) data.getParcelableExtra(EXTRA_MESSAGE);
            String tagType = data.getStringExtra(EXTRA_TYPE);
            int maxSize = data.getIntExtra(EXTRA_SIZE, 0);

            try {
                Message message = new Message(ndefMessage);
                showTagInfo(tagType, maxSize, message);
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }

        if (Activity.RESULT_CANCELED == resultCode) {
            clearTagInfo();
        }
    }

    private void showTagInfo(String tagType, int maxSize, Message message) {
        textViewTagType.setText(tagType);
        textViewMaxSize.setText(maxSize + " bytes");

        if (message != null && !message.isEmpty()) {
            ArrayAdapter<? extends Object> adapter = new NdefRecordAdapter(getActivity(), message);
            listViewContent.setAdapter(adapter);
        } else {
            clearTagInfo();
        }

        if (message.get(0) instanceof UriRecord) {
            UriRecord uriRecord = (UriRecord) message.get(0);
            Uri uri = uriRecord.getUri();

            if (uri.getScheme().equals("tel")) {
                startActivity(new Intent(Intent.ACTION_CALL, uriRecord.getUri()));
            }
        }
    }

    private void clearTagInfo() {
        textViewTagType.setText("");
        textViewMaxSize.setText("");
        listViewContent.setAdapter(null);
    }

}
