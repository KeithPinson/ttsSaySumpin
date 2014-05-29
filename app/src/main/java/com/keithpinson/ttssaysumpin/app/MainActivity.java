package com.keithpinson.ttssaysumpin.app;

import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    static final String LOG_TAG = "MainActivity";

    private Button playAgainButton;
    private EditText textToSay;
    private TextToSpeech textToSpeech;
    private Boolean promptCleared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //
        // EditText Field
        //
        textToSay = (EditText)findViewById(com.keithpinson.ttssaysumpin.app.R.id.textToSay);


        //
        // Text-to-Speech Button
        //
        playAgainButton = (Button) findViewById(com.keithpinson.ttssaysumpin.app.R.id.SayIt);
        playAgainButton.setOnClickListener(this);


        // Create the Text-to-Speech controller
        textToSpeech = new TextToSpeech(this, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onInit(int status) {
        if (Build.VERSION.SDK_INT >= 15) {
            Log.d(LOG_TAG, "OnInit() SDK_INT >= 15");

            // This is the preferred method since API 15
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                }
            });
        } else {
            Log.d(LOG_TAG, "OnInit() SDK_INT < 15");

            // Fall back to the deprecated method
            textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                @Override
                public void onUtteranceCompleted(String arg0) {
//                    finish();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onClick(View v) {
        speak();
    }

    private void speak() {

        try {
            final String sayString = textToSay.getText().toString();

            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... params) {

                    HashMap<String, String> ttsParams = new HashMap<String, String>();
//                    ttsParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ".");  // May have been required at one point
                    textToSpeech.speak(sayString, TextToSpeech.QUEUE_FLUSH, ttsParams);

                    return null;
                }
            }.execute((Void)null);
        }
        catch( NullPointerException npe ) {
            // Nothing to say...
        }
    }
}
