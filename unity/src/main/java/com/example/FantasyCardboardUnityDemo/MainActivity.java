package com.example.FantasyCardboardUnityDemo;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.plugins.unity.UnityCardboardActivity;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import java.util.logging.Handler;

//import android.support.v7.app.ActionBarActivity;
//import android.view.Menu;
//import android.view.MenuItem;


public class MainActivity extends Activity implements OnClickListener {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private SpeechRecognizer sr;
    private static final String TAG = "MyStt3Activity";

    private Button startButton, recordButton;
    private MediaRecorder myAudioRecorder;
    private MediaPlayer m;
    private String outputFile = null;
    private Boolean isPaused = false;
//    private Boolean isDonePlaying = true;

    private  final int delayTime = 5000;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test Tag" ,"Started main activity");
        startButton = (Button)findViewById(R.id.start_Button);
//        recordButton = (Button)findViewById(R.id.record_Button);

        // Set up recording
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/myrecording.3gp";

        checkVoiceRecognition();
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());


        m = new MediaPlayer();
//        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            public void onCompletion(MediaPlayer mp){
//                isDonePlaying = true;
//            }
//        });

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }

//    public void onUserInteraction() {
//        myHandler.removeCallbacks(closeControls);
//        myHandler.postDelayed(closeControls, delayTime);
//    }

    private Runnable closeControls = new Runnable() {
        public void run() {
            Toast.makeText(getApplicationContext(), "10 secoonds has gone by buddy!",
                    Toast.LENGTH_LONG).show();

//            sr.stopListening();

            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder  = null;
            Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                    Toast.LENGTH_LONG).show();

            UnityCardboardActivity.getActivity().onBackPressed();
            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
            MainActivity.this.startActivity(mainIntent);
            sr.stopListening();
        }
    };

    @Override
    public void onClick(View v) {
        Log.d("Test Tag" ,"Something clicked");
        if (v == startButton) {
            Toast.makeText(getApplicationContext(), "Better start up Unity!",
                    Toast.LENGTH_LONG).show();
            Log.d("Test Tag" ,"StartButton clicked");
        }
    }

//    public void record(View v) {
//        Toast.makeText(getApplicationContext(), "I will record you son!",
//                Toast.LENGTH_SHORT).show();
//        recordButton.setEnabled(true);
//        try {
//            myAudioRecorder.prepare();
//            myAudioRecorder.start();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
//    }
//    public void stop(View view){
//        myAudioRecorder.stop();
//        myAudioRecorder.release();
//        myAudioRecorder  = null;
////        stop.setEnabled(false);
////        play.setEnabled(true);
//        Toast.makeText(getApplicationContext(), "Audio recorded successfully",
//                Toast.LENGTH_LONG).show();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    public void play(View view) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException{
        if (!isPaused) {
            m.setDataSource(outputFile);
            m.prepare();
            m.start();
            Toast.makeText(getApplicationContext(), "Start playing audio", Toast.LENGTH_LONG).show();
        } else {
            m.start();
            Toast.makeText(getApplicationContext(), "Re playing audio", Toast.LENGTH_LONG).show();
        }
    }

    public void recognize(View view) {
        Toast.makeText(getApplicationContext(), "Recognizing Voice",
                Toast.LENGTH_SHORT).show();
        speak2();
    }

    public void stopRecognize(View view) { //stopRecognize
        Toast.makeText(getApplicationContext(), "Stopping recognizing Voice",
                Toast.LENGTH_SHORT).show();
        sr.stopListening();
    }

    public void pause(View view) {
        m.pause();
        isPaused = true;
    }

    public void test(View v){
        Log.d("Test Tag", "Something clicked");
        /*Toast.makeText(getApplicationContext(), "Hopefully starting up Unity!",
                Toast.LENGTH_SHORT).show();
*/
        Toast.makeText(getApplicationContext(), "Initializing thread",
                Toast.LENGTH_SHORT).show();
//        new backgroundThread().execute();
//        Thread xyz = new Thread(new PhotoDecodeRunnable());
//        Toast.makeText(getApplicationContext(), "Starting thread",
//                Toast.LENGTH_SHORT).show();
//        xyz.start()

        myHandler.postDelayed(closeControls, delayTime);
        try {
            speak2();
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
//        Intent myIntent = new Intent(MainActivity.this, UnityPlayerNativeActivity.class);
        Intent unityIntent = new Intent(MainActivity.this, UnityCardboardActivity.class);
        MainActivity.this.startActivity(unityIntent);
//        Intent intent = new Intent(main.this, .class);
//        startActivity(intent);
//        finish();
//        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.example.FantasyCardboardUnityDemo");
//        startActivity(LaunchIntent);
//        try {
//            wait(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        finish();
    }

//    private class backgroundThread extends AsyncTask {
//
//
//        protected void onProgressUpdate(Integer... progress) {
//
//        }
//
//        protected void onPostExecute(Long result) {
//
//        }
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            try {
//                Toast.makeText(getApplicationContext(), "Getting into wait",
//                        Toast.LENGTH_SHORT).show();
////                Intent unityIntent = new Intent(MainActivity.this, UnityCardboardActivity.class);
////                MainActivity.this.startActivity(unityIntent);
//                wait(5000);
//                Toast.makeText(getApplicationContext(), "wait is over .. try to close activity",
//                        Toast.LENGTH_SHORT).show();
//                UnityCardboardActivity.getActivity().finish();
//            } catch (InterruptedException e) {
//                Toast.makeText(getApplicationContext(), "Exception happened: " + e.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    public class PhotoDecodeRunnable implements Runnable {
//        @Override
//        public void run() {
//        /*
//         * Code you want to run on the thread goes here
//         */
//            try {
//                Toast.makeText(getApplicationContext(), "Getting into wait",
//                        Toast.LENGTH_SHORT).show();
////                Intent unityIntent = new Intent(MainActivity.this, UnityCardboardActivity.class);
////                MainActivity.this.startActivity(unityIntent);
//                wait(5000);
//                Toast.makeText(getApplicationContext(), "wait is over .. try to close activity",
//                        Toast.LENGTH_SHORT).show();
//                UnityCardboardActivity.getActivity().finish();
//            } catch (InterruptedException e) {
//                Toast.makeText(getApplicationContext(), "Exception happened: " + e.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    }

    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
//            mbtSpeak.setEnabled(false);
//            mbtSpeak.setText("Voice recognizer not present")
            Toast.makeText(this, "Voice recognizer not present",
                    Toast.LENGTH_SHORT).show();
        }
    }

//    public void speak() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//        // Specify the calling package to identify your application
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
//                .getPackage().getName());
//
//        // Given an hint to the recognizer about what the user is going to say
//        //There are two form of language model available
//        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
//        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        //Start the Voice recognizer activity for the result.
//        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
//
//            //If Voice recognition is successful then it returns RESULT_OK
//            if(resultCode == RESULT_OK) {
//
//                ArrayList<String> textMatchList = data
//                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//
//                showToastMessage("Return text:" + textMatchList);
//
//                //Result code for various error.
//            }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
//                showToastMessage("Audio Error");
//            }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
//                showToastMessage("Client Error");
//            }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
//                showToastMessage("Network Error");
//            }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
//                showToastMessage("No Match");
//            }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
//                showToastMessage("Server Error");
//            }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    /**
//     * Helper method to show the toast message
//     **/
//    void showToastMessage(String message){
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }

    public void speak2() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        sr.startListening(intent);
        Log.i("111111","11111111");
    }

    class listener implements RecognitionListener
    {
        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }
        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech()
        {
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
            Log.d(TAG,  "error " +  error);
        }
        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(TAG, "result " + data.get(i));
                str += data.get(i);
            }
            Toast.makeText(getApplicationContext(), "results: "+String.valueOf(data.size()), Toast.LENGTH_LONG).show();
//            mText.setText("results: "+String.valueOf(data.size()));
        }
        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }
}

