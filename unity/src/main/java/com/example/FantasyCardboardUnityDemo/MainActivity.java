package com.example.FantasyCardboardUnityDemo;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
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
    private Button playButton, pauseButton;

    protected static AudioManager mAudioManager;

    private MediaRecorder myAudioRecorder;
    private MediaPlayer m;
    private String outputFile = null;
    private Boolean isPaused = false;
    private Uri audioUri = null;
    private Intent recognizerIntent; //
//    private Boolean isDonePlaying = true;

    private int delayTime = 5000;
    private Handler myHandler = new Handler();

    private int mBindFlag;
    private Messenger mServiceMessenger;

    public String strOfResults = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test Tag" ,"Started main activity");
        startButton = (Button)findViewById(R.id.start_Button);
        playButton = (Button)findViewById(R.id.play_Button);
        pauseButton = (Button)findViewById(R.id.pause_Button);

        // Set up recording
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/myrecording.3gp";

        checkVoiceRecognition();
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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

    @Override
    public void onStart() {
//        playButton.setEnabled(false);
//        pauseButton.setEnabled(false);
        super.onStart();
    }

    private Runnable closeControls = new Runnable() {
        public void run() {
            Toast.makeText(getApplicationContext(), "10 secoonds has gone by buddy!",
                    Toast.LENGTH_LONG).show();

            boolean shoudRecord = ((MyApplication) MainActivity.this.getApplication()).getShouldRecord();
            if (!shoudRecord) {
                sr.stopListening();
                mAudioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
                mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
            } else {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;
            }
            Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                    Toast.LENGTH_LONG).show();

            UnityCardboardActivity.getActivity().onBackPressed();
            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
            MainActivity.this.startActivity(mainIntent);
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

    public void startService(View view) {
        Intent intent = new Intent(MainActivity.this, PocketSphinxActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(MainActivity.this, SpeechService.class);
//        MainActivity.this.startService(intent);
//        mBindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
//
//        super.onStart();
//        bindService(new Intent(this, SpeechService.class), mServiceConnection, mBindFlag);
    }

    public void stopService(View view) {
        super.onStop();

//        if (mServiceMessenger != null) {
//            unbindService(mServiceConnection);
//            mServiceMessenger = null;
//        }
        //super.onStop();
    }

//    private final ServiceConnection mServiceConnection = new ServiceConnection()
//    {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service)
//        {
//            Log.d(TAG, "onServiceConnected"); //$NON-NLS-1$
//
//            mServiceMessenger = new Messenger(service);
//            Message msg = new Message();
//            msg.what = SpeechService.MSG_RECOGNIZER_START_LISTENING;
//
//            try
//            {
//                mServiceMessenger.send(msg);
//            }
//            catch (RemoteException e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name)
//        {
//            Log.d(TAG, "onServiceDisconnected"); //$NON-NLS-1$
//            mServiceMessenger = null;
//        }
//
//    }; // mServiceConnection

    public void play(View view) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException{
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
        if (!isPaused) {
            m.setDataSource(outputFile);
//            m.setDataSource(this, audioUri);
            m.prepare();
            m.start();
            Toast.makeText(getApplicationContext(), "Start playing audio", Toast.LENGTH_LONG).show();
        } else {
            m.start();
            Toast.makeText(getApplicationContext(), "Re playing audio", Toast.LENGTH_LONG).show();
        }
    }

    public void segue(View view) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }

    public void pause(View view) {
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        m.pause();
        isPaused = true;
    }

    public void test(View v){
        Log.d("Test Tag", "Something clicked");
        /*Toast.makeText(getApplicationContext(), "Hopefully starting up Unity!",
                Toast.LENGTH_SHORT).show();
*/
        int minuteValue = ((MyApplication) this.getApplication()).getMinuteTime();
        int secondValue = ((MyApplication) this.getApplication()).getSecondTime();
        delayTime = minuteValue + secondValue;
        Toast.makeText(getApplicationContext(), "Presentation Time: " + delayTime,
                Toast.LENGTH_SHORT).show();

        myHandler.postDelayed(closeControls, delayTime);
            boolean shoudRecord = ((MyApplication) this.getApplication()).getShouldRecord();

            if (!shoudRecord) {
                speak2();
            } else {
                try {
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
//                } catch (IllegalStateException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();}
                } catch (IOException e) {
                 // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
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
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, delayTime);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, delayTime);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, delayTime);


//        recognizerIntent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");//
//        recognizerIntent.putExtra("android.speech.extra.GET_AUDIO", true);//

        mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,2);
        sr.startListening(recognizerIntent);
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
            sr.startListening(recognizerIntent);
            Log.d(TAG, "onEndofSpeech");
        }
        public void onError(int error)
        {
//            mAudioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
//            mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
            Log.d(TAG,  "error " +  error);
        }
        public void onResults(Bundle results)
        {

            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            //for (int i = 0; i < data.size(); i++)
            //{
            strOfResults += data.get(0);
            Log.d(TAG, "result " + strOfResults);
            Toast.makeText(getApplicationContext(), "result " + strOfResults, Toast.LENGTH_LONG).show();

            //}

            audioUri = recognizerIntent.getData();
            //Toast.makeText(getApplicationContext(), "results: "+String.valueOf(data.size()), Toast.LENGTH_LONG).show();
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

