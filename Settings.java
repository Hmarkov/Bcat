package com.example.kri.cat2;
//srackoverflow.com/questions/27148761/how-to-write-multiple-strings-in-to-nfc-tag-in-android

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.Locale;

import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Settings extends AppCompatActivity {
    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writetagFilters[];
    Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button btnSave = (Button) findViewById(R.id.btn_save);
        EditText name = (EditText) findViewById(R.id.textnome);
        EditText number = (EditText) findViewById(R.id.textnumero);
        String xyz = name.getText().toString();
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 try {
                     if (tag = null) {

                     }else{
                         write(name.getText().toString(), tag);
                         write(number.getText().toString(), tag);
                     }
                 }catch (IOException e) {


                } catch (FormatException e) {

                 }
            }
        });

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter TagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        TagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writetagFilters = new IntentFilter[]{TagDetected};
    }

    private void write(String text, Tag tag)  throws IOException, FormatException
    {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();

    }

    public NdefRecord createRecord(String content) throws UnsupportedEncodingException
    {
        String lang = "en";
        byte[] textBytes = content.getBytes();
        byte[] langBytes = Locale.getDefault().getLanguage("UTF-8");
        int langlenght = langBytes.length;
        int textlenght = textBytes.length;
        byte[] payload = new byte[1 + langlenght + textlenght];
        payload[0] = (byte) langlenght;
        System.arraycopy(langBytes, 0, payload, 1, langlenght);
        System.arraycopy(textBytes, 0, payload, 1, langlenght, textlenght);
        NdefRecord nfcRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "text/vcard".getBytes(),
                new byte[0],
                payload);
        return nfcRecord;
    }

    public createNdefMessagge(String content)  throws UnsupportedEncodingException
    {
        NdefRecord ndefRecord = createRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }
    public btnReadWriteClick()
    {

    }


}

