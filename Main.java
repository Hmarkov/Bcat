package com.example.kri.cat2;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;


import android.widget.Toast;


public class Main extends AppCompatActivity {
    NfcAdapter NfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
            }
        });
        NfcAdapter nfcAdapter= android.nfc.NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter!=null&&nfcAdapter.isEnabled())
        {
            Toast.makeText(this, "NFC Available", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"NFC not available :( ",Toast.LENGTH_LONG).show();
        }
        }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDistpatchSystem();
    }
    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra(android.nfc.NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NFC Intent", Toast.LENGTH_SHORT).show();
            Tag tag=intent.getParcelableExtra(android.nfc.NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enableForegroundDistpatchSystem()
    {
        Intent intent = new Intent(this, Main.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[] {};
        NfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilter,null);
    }


    public void disableForegroundDispatchSystem(){
        NfcAdapter.disableForegroundDispatch(this);
    }


    public void NewActivity(View view) {
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }

    private void FormatTag(Tag tag,NdefMessage ndefMessage){
        try
        {
            NdefFormatable ndefFormat =NdefFormatable.get(tag);
            if(ndefFormat==null) {
                Toast.makeText(this, "This tag is not ndef format", Toast.LENGTH_SHORT).show();
                return;
            }
            ndefFormat.connect();
            ndefFormat.format(ndefMessage);
            ndefFormat.close();
            Toast.makeText(this, "Tag written", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("formatTag",e.getMessage());
        }
    }

    private void NdefMessage(Tag tag,NdefMessage ndefMessage)
    {
        try {
            if (tag==null){
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }
            Ndef ndef=Ndef.get(tag);
            if(ndef==null){
                //format tag with ndef format and writes the message
                FormatTag(tag,ndefMessage);
            }else{
                ndef.connect();
                if(!ndef.isWritable())
                {
                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Tag written", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            Log.e("WriteNdefMessage",e.getMessage());
        }
    }
}
